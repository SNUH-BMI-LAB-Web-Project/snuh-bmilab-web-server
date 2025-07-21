package com.bmilab.backend.domain.board.service;

import com.bmilab.backend.domain.board.dto.query.GetAllBoardsQueryResult;
import com.bmilab.backend.domain.board.dto.request.BoardRequest;
import com.bmilab.backend.domain.board.dto.response.BoardDetail;
import com.bmilab.backend.domain.board.dto.response.BoardFindAllResponse;
import com.bmilab.backend.domain.board.entity.Board;
import com.bmilab.backend.domain.board.entity.BoardCategory;
import com.bmilab.backend.domain.board.entity.BoardFile;
import com.bmilab.backend.domain.board.exception.BoardErrorCode;
import com.bmilab.backend.domain.board.repository.BoardFileRepository;
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
import com.bmilab.backend.domain.board.dto.response.BoardFindAllResponse.BoardSummary;

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
    private final FileInformationRepository fileInformationRepository;
    private final BoardFileRepository boardFileRepository;
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
    }

    @Transactional
    public void updateBoard(Long userId, Long boardId, BoardRequest request) {
        User user = userService.findUserById(userId);

        Board board = findBoardById(boardId);

        validateUserIsReportAuthor(user, board);

        BoardCategory category = boardCategoryService.getBoardCategoryById(request.boardCategoryId());

        board.update(
                category,
                request.title(),
                request.content()
        );
    }

    @Transactional
    public void deleteBoard(Long userId, Long boardId) {
        User user = userService.findUserById(userId);

        Board board = findBoardById(boardId);

        validateUserIsReportAuthor(user, board);

        fileService.deleteAllFileByDomainTypeAndEntityId(FileDomainType.BOARD, board.getId());

        boardRepository.delete(board);
    }

    @Transactional
    public void deleteBoardFile(Long userId, Long boardId, UUID fileId) {
        User user = userService.findUserById(userId);

        Board board = findBoardById(boardId);

        validateUserIsReportAuthor(user, board);

        FileInformation file = fileInformationRepository.findById(fileId)
                .orElseThrow(() -> new ApiException(FileErrorCode.FILE_NOT_FOUND));

        BoardFile boardFile =  boardFileRepository.findByFileInformation(file)
                .orElseThrow(() -> new ApiException(BoardErrorCode.BOARD_FILE_NOT_FOUND));

        s3Service.deleteFile(file.getUploadUrl());

        boardFileRepository.delete(boardFile);
    }


    public BoardFindAllResponse getAllBoards(
        Long userId,
        String search,
        String category,
        Pageable pageable
    ) {

        Page<GetAllBoardsQueryResult> queryResults = boardRepository.findAllByFiltering(
                userId,
                search,
                category,
                pageable
        );

        return BoardFindAllResponse
                .builder()
                .boards(
                        queryResults.getContent()
                                .stream()
                                .map(BoardSummary::from)
                                .toList()
                )
                .totalPage(queryResults.getTotalPages())
                .build();
    }

    public BoardDetail getBoardDetailById(Long userId, Long boardId) {
        User user = userService.findUserById(userId);

        Board board = findBoardById(boardId);

        validateUserIsReportAuthor(user, board);

        List<BoardFile> boardFiles = boardFileRepository.findAllByBoardId(boardId);

        return BoardDetail.from(board, boardFiles);
    }

    private void validateUserIsReportAuthor(User user, Board board) {
        if(!board.isAuthor(user)){
            throw new ApiException(BoardErrorCode.BOARD_ACCESS_DENIED);
        }
    }
}
