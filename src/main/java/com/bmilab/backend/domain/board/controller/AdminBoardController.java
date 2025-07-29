package com.bmilab.backend.domain.board.controller;

import com.bmilab.backend.domain.board.dto.request.BoardPinRequest;
import com.bmilab.backend.domain.board.service.BoardService;
import com.bmilab.backend.global.security.UserAuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/boards")
@RequiredArgsConstructor
public class AdminBoardController implements AdminBoardApi {

    private final BoardService boardService;

    @PatchMapping("/{boardId}/pin")
    public ResponseEntity<Void> updateBoardPinStatus(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long boardId,
            @RequestBody BoardPinRequest request
    ){
        boardService.updateBoardPinStatus(userAuthInfo.getUserId(), boardId, request);

        return ResponseEntity.ok().build();
    }

}
