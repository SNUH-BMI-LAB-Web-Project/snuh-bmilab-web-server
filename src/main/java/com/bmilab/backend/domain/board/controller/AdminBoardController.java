package com.bmilab.backend.domain.board.controller;

import com.bmilab.backend.domain.board.dto.request.PinStatusRequest;
import com.bmilab.backend.domain.board.service.AdminBoardService;
import com.bmilab.backend.global.security.UserAuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/board")
@RequiredArgsConstructor
public class AdminBoardController implements AdminBoardApi {

    private final AdminBoardService adminBoardService;

    @PutMapping("/{boardId}")
    public ResponseEntity<Void> updateBoardPinStatus(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long boardId,
            @RequestBody PinStatusRequest request
    ){
        adminBoardService.updateBoardStatus(userAuthInfo.getUserId(), boardId, request);

        return ResponseEntity.ok().build();
    }

}
