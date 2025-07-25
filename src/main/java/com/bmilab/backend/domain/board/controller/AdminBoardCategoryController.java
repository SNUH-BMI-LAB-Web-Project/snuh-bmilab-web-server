package com.bmilab.backend.domain.board.controller;

import com.bmilab.backend.domain.board.dto.request.BoardCategoryReqeust;
import com.bmilab.backend.domain.board.service.BoardCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/boards/categories")
@RequiredArgsConstructor
public class AdminBoardCategoryController implements AdminBoardCategoryApi {

    private final BoardCategoryService boardCategoryService;

    @PostMapping
    public ResponseEntity<Void> createBoardCategory(@RequestBody @Valid BoardCategoryReqeust request){
        boardCategoryService.createBoardCategory(request);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> updateBoardCategory(
            @PathVariable Long categoryId,
            @RequestBody @Valid BoardCategoryReqeust request
    ){
        boardCategoryService.updateById(categoryId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteBoardCategory(
            @PathVariable Long categoryId
    ){
        boardCategoryService.deleteById(categoryId);

        return ResponseEntity.ok().build();
    }
}
