package com.bmilab.backend.domain.board.dto.response;

import com.bmilab.backend.domain.board.entity.BoardCategory;

import java.util.List;

public record BoardCategoryFindAllResponse(
        List<BoardCategorySummary> categories
) {
    public static BoardCategoryFindAllResponse of(List<BoardCategory> categories) {
        return new BoardCategoryFindAllResponse(
                categories.stream()
                        .map(BoardCategorySummary::from)
                        .toList()
        );
    }
}
