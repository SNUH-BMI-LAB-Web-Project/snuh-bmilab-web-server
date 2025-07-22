package com.bmilab.backend.domain.board.service;

import com.bmilab.backend.domain.board.entity.BoardCategory;
import com.bmilab.backend.domain.board.exception.BoardErrorCode;
import com.bmilab.backend.domain.board.repository.BoardCategoryRepository;
import com.bmilab.backend.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCategoryService {

    private final BoardCategoryRepository boardCategoryRepository;

    public BoardCategory getBoardCategoryById(Long categoryId) {
        return boardCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(BoardErrorCode.CATEGORY_NOT_FOUND));
    }
}
