package com.bmilab.backend.domain.board.service;

import com.bmilab.backend.domain.board.dto.query.GetAllBoardsQueryResult;
import com.bmilab.backend.domain.board.dto.request.BoardPinRequest;
import com.bmilab.backend.domain.board.dto.request.BoardRequest;
import com.bmilab.backend.domain.board.dto.response.BoardDetail;
import com.bmilab.backend.domain.board.dto.response.BoardFindAllResponse;
import com.bmilab.backend.domain.board.dto.response.BoardFindAllResponse.BoardSummary;
import com.bmilab.backend.domain.board.entity.Board;
import com.bmilab.backend.domain.board.entity.BoardCategory;
import com.bmilab.backend.domain.board.exception.BoardErrorCode;
import com.bmilab.backend.domain.board.repository.BoardRepository;
import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.exception.FileErrorCode;
import com.bmilab.backend.domain.file.repository.FileInformationRepository;
import com.bmilab.backend.domain.file.service.FileService;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.service.UserService;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.external.s3.S3Service;
import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.PessimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;
    private final BoardCategoryService boardCategoryService;
    private final FileService fileService;
    private final PlatformTransactionManager txManager;


    public Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(BoardErrorCode.BOARD_NOT_FOUND));
    }

    @Transactional
    public void createBoard(Long userId, BoardRequest request) {
        User user = userService.findUserById(userId);

        BoardCategory category = boardCategoryService.getBoardCategoryById(request.boardCategoryId());

        Board board = Board.builder()
                .author(user)
                .category(category)
                .title(request.title())
                .content(request.content())
                .build();

        boardRepository.save(board);

        fileService.updateAllFileDomainByIds(request.fileIds(), FileDomainType.BOARD, board.getId());
        fileService.updateAllFileDomainByIds(request.imageFileIds(), FileDomainType.BOARD_IMAGE, board.getId());
    }

    @Transactional
    public void updateBoard(Long userId, Long boardId, BoardRequest request) {
        User user = userService.findUserById(userId);

        Board board = findBoardById(boardId);

        validateBoardAccessPermission(user, board);

        BoardCategory category = boardCategoryService.getBoardCategoryById(request.boardCategoryId());

        board.update(
                category,
                request.title(),
                request.content()
        );

        //신규 파일만 인식하기 + 이미지 업데이트 처리
        fileService.syncFiles(request.fileIds(), FileDomainType.BOARD, board.getId());
        fileService.updateAllFileDomainByIds(request.fileIds(), FileDomainType.BOARD, board.getId());
    }

    @Transactional
    public void deleteBoard(Long userId, Long boardId) {
        User user = userService.findUserById(userId);

        Board board = findBoardById(boardId);

        validateBoardAccessPermission(user, board);

        fileService.deleteAllFileByDomainTypeAndEntityId(FileDomainType.BOARD, board.getId());

        boardRepository.delete(board);
    }

    @Transactional
    public void deleteBoardFile(Long userId, Long boardId, UUID fileId) {
        User user = userService.findUserById(userId);

        Board board = findBoardById(boardId);

        validateBoardAccessPermission(user, board);

        fileService.deleteFile(FileDomainType.BOARD, fileId);
    }

    public BoardFindAllResponse getAllBoards(
        Long userId,
        String search,
        String category,
        Pageable pageable
    ) {
        Page<GetAllBoardsQueryResult> regularBoardsResult = boardRepository.findAllByFiltering(
                userId,
                search,
                category,
                pageable
        );

        return BoardFindAllResponse
                .builder()
                .boards(
                        regularBoardsResult.getContent()
                                .stream()
                                .map(BoardSummary::from)
                                .toList()
                )
                .totalPage(regularBoardsResult.getTotalPages())
                .build();
    }

    public BoardDetail getBoardDetailById(Long userId, Long boardId) {
        User user = userService.findUserById(userId);

        Board board = findBoardById(boardId);

        validateBoardAccessPermission(user, board);

        List<FileInformation> files = fileService.findAllByDomainTypeAndEntityId(FileDomainType.BOARD, board.getId());

        return BoardDetail.from(board, files);
    }

    public void updateBoardPinStatus(Long boardId, BoardPinRequest request) {

        TransactionTemplate tt = new TransactionTemplate(txManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                tt.executeWithoutResult(status -> {
                    Board board;
                    try {
                        board = boardRepository.findByIdWithPessimisticLock(boardId)
                                .orElseThrow(() -> new ApiException(BoardErrorCode.BOARD_NOT_FOUND));
                    } catch (PessimisticLockException | LockTimeoutException e) {
                        throw new CannotAcquireLockException("Lock acquisition failed", e);
                    }

                    boolean targetPin = request.isPinned();
                    boolean alreadyPinned = board.isPinned();
                    if (targetPin && !alreadyPinned) {
                        List<Board> pinned = boardRepository.findAllPinnedForUpdate();
                        if (pinned.size() >= 5) {
                            throw new ApiException(BoardErrorCode.BOARD_PIN_LIMIT_EXCEEDED);
                        }
                    }

                    board.setPinned(targetPin);
                    boardRepository.save(board);
                });
                return; // success
            } catch (PessimisticLockingFailureException e) {
                if (attempt == 3) {
                    throw new ApiException(BoardErrorCode.BOARD_PIN_VERSION_CONFLICT);
                }
                try {
                    Thread.sleep(50L * attempt); // tiny backoff: 50ms, 100ms
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new ApiException(BoardErrorCode.BOARD_PIN_VERSION_CONFLICT);
                }
            }
        }
    }

    private void validateBoardAccessPermission(User user, Board board) {
        if(!board.canBeEditedBy(user)){
            throw new ApiException(BoardErrorCode.BOARD_ACCESS_DENIED);
        }
    }
}
