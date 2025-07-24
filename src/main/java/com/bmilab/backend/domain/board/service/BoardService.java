package com.bmilab.backend.domain.board.service;

import com.bmilab.backend.domain.board.dto.query.GetAllBoardsQueryResult;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;
    private final BoardCategoryService boardCategoryService;
    private final FileService fileService;
    private final FileInformationRepository fileInformationRepository;
    private final S3Service s3Service;

    public Board findBoardById(Long boardId){
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

        List<FileInformation> files = fileInformationRepository.findAllById(request.fileIds());

        files.forEach(file -> file.updateDomain(FileDomainType.BOARD, board.getId()));
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

        List<FileInformation> files = fileInformationRepository.findAllById(request.fileIds());

        files.forEach(file -> file.updateDomain(FileDomainType.BOARD, board.getId()));
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

        FileInformation file = fileInformationRepository.findById(fileId)
                .orElseThrow(() -> new ApiException(FileErrorCode.FILE_NOT_FOUND));

        s3Service.deleteFile(file.getUploadUrl());

        fileInformationRepository.deleteById(fileId);
    }


    public BoardFindAllResponse getAllBoards(
        Long userId,
        String search,
        String category,
        Pageable pageable
    ) {
        List<Board> pinnedBoards = boardRepository.findAllByIsPinnedTrueOrderByCreatedAtDesc();

        Page<GetAllBoardsQueryResult> regularBoardsResult = boardRepository.findAllByFiltering(
                userId,
                search,
                category,
                pageable
        );

        return BoardFindAllResponse
                .builder()
                .pinnedBoards(
                        pinnedBoards.stream()
                                .map(BoardSummary::from)
                                .collect(Collectors.toList())

                )
                .regularBoards(
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

        List<FileInformation> files = fileInformationRepository.findAllByDomainTypeAndEntityId(FileDomainType.BOARD, board.getId());

        return BoardDetail.from(board, files);
    }

    private void validateBoardAccessPermission(User user, Board board) {
        if(!board.canBeEditedBy(user)){
            throw new ApiException(BoardErrorCode.BOARD_ACCESS_DENIED);
        }
    }
}
