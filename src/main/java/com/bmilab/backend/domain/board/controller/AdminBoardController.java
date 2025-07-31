package com.bmilab.backend.domain.board.controller;

import com.bmilab.backend.domain.board.dto.request.BoardPinRequest;
import com.bmilab.backend.domain.board.service.BoardService;
import com.bmilab.backend.global.annotation.OnlyAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@OnlyAdmin
@RestController
@RequestMapping("/admin/board")
@RequiredArgsConstructor
public class AdminBoardController implements AdminBoardApi {

    private final BoardService boardService;

    @PatchMapping("/{boardId}/pin")
    public ResponseEntity<Void> updateBoardPinStatus(
            @PathVariable Long boardId,
            @RequestBody BoardPinRequest request
    ){
        boardService.updateBoardPinStatus(boardId, request);

        return ResponseEntity.ok().build();
    }

}
