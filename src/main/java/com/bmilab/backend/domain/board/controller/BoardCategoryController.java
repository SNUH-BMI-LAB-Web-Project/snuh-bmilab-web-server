package com.bmilab.backend.domain.board.controller;

import com.bmilab.backend.domain.board.dto.response.BoardCategoryFindAllResponse;
import com.bmilab.backend.domain.board.service.BoardCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/boards/categories")
@RequiredArgsConstructor
public class BoardCategoryController implements BoardCategoryApi {
    private final BoardCategoryService boardCategoryService;

    @GetMapping
    public ResponseEntity<BoardCategoryFindAllResponse> getAllBoardCategories(){
        return ResponseEntity.ok(boardCategoryService.getAllBoardCategories());
    }
}
