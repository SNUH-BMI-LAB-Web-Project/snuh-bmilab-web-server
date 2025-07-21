package com.bmilab.backend.domain.board.controller;

import com.bmilab.backend.domain.board.dto.request.BoardRequest;
import com.bmilab.backend.domain.board.dto.response.BoardDetail;
import com.bmilab.backend.domain.board.dto.response.BoardFindAllResponse;
import com.bmilab.backend.domain.board.entity.BoardCategory;
import com.bmilab.backend.domain.board.service.BoardService;
import com.bmilab.backend.global.security.UserAuthInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController implements BoardApi {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Void> createBoard(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid BoardRequest request
    ){
            boardService.createBoard(userAuthInfo.getUserId(), request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping ("/{boardId}")
    public ResponseEntity<Void> updateBoard(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long boardId,
            @RequestBody @Valid BoardRequest request
    ){
        boardService.updateBoard(userAuthInfo.getUserId(), boardId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping ("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long boardId
    ){
        boardService.deleteBoard(userAuthInfo.getUserId(), boardId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{boardId}/file/{fileId}")
    public ResponseEntity<Void> deleteBoardFile(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long boardId,
            @PathVariable UUID fileId
    ){
        boardService.deleteBoardFile(userAuthInfo.getUserId(), boardId, fileId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<BoardFindAllResponse> getAllBoards(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable
    ){
        return ResponseEntity.ok(boardService.getAllBoards(
                userAuthInfo.getUserId(),
                search,
                category,
                pageable
        ));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDetail> getBoardById(
        @AuthenticationPrincipal UserAuthInfo userAuthInfo,
        @PathVariable Long boardId
    ){
        return  ResponseEntity.ok(boardService.getBoardDetailById(userAuthInfo.getUserId(), boardId));
    }
}
