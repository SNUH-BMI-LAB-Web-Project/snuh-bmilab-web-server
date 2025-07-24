package com.bmilab.backend.domain.board.service;

import com.bmilab.backend.domain.board.dto.request.PinStatusRequest;
import com.bmilab.backend.domain.board.entity.Board;
import com.bmilab.backend.domain.board.exception.BoardErrorCode;
import com.bmilab.backend.domain.board.repository.BoardRepository;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.enums.Role;
import com.bmilab.backend.domain.user.service.UserService;
import com.bmilab.backend.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminBoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;

    public Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(BoardErrorCode.BOARD_NOT_FOUND));
    }

    @Transactional
    public void updateBoardStatus(Long userId, Long boardId, PinStatusRequest request) {

        User user = userService.findUserById(userId);

        Board board = findBoardById(boardId);

        validateBoardStatusAccess(user);

        board.setPinned(request.isPinned());

        boardRepository.save(board);
    }

    private void validateBoardStatusAccess(User user) {
        if (!(user.getRole() == Role.ADMIN)){
            throw new ApiException(BoardErrorCode.BOARD_STATUS_ACCESS_DENIED);
        }
    }

}
