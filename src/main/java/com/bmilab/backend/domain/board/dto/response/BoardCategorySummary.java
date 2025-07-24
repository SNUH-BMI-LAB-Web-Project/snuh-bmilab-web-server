package com.bmilab.backend.domain.board.dto.response;

import com.bmilab.backend.domain.board.entity.BoardCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record BoardCategorySummary(
        @Schema(description = "게시판 분야 ID", example = "1.0")
        Long boardCategoryId,

        @Schema(description = "게시판 분야 이름", example = "공지사항")
        String name
) {
        public static BoardCategorySummary from(BoardCategory boardCategory) {
                if(boardCategory == null) {
                        return null;
                }
                return BoardCategorySummary.builder()
                        .boardCategoryId(boardCategory.getId())
                        .name(boardCategory.getName())
                        .build();
        }
}
