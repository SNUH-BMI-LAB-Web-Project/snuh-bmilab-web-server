package com.bmilab.backend.domain.board.dto.response;

import com.bmilab.backend.domain.board.dto.query.GetAllBoardsQueryResult;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BoardFindAllResponse(
        List<BoardSummary> boards,
        int totalPage
) {
    @Builder
    public record BoardSummary(
            @Schema(description = "게시판 ID", example = "1")
            Long boardId,

            @Schema(description = "작성자 정보")
            UserSummary author,

            @Schema(description = "게시판 분야 정보")
            BoardCategorySummary boardCategory,

            @Schema(description = "게시판 제목", example = "신입 연구원 근무 수칙 안내")
            String title,

            @Schema(description = "조회수", example = "1")
            Integer viewCount,

            @Schema(description = "게시글 생성 일시", example = "2025-07-28T10:30:00")
            LocalDateTime createdAt,

            @Schema(description = "게시글 고정 여부")
            boolean isPinned
    ){
        public static BoardSummary from(GetAllBoardsQueryResult queryResults) {
            return BoardSummary.builder()
                    .boardId(queryResults.getBoardId())
                    .author(UserSummary.from(queryResults.getAuthor()))
                    .boardCategory(BoardCategorySummary.from(queryResults.getBoardCategory()))
                    .title(queryResults.getTitle())
                    .viewCount(queryResults.getViewCount())
                    .createdAt(queryResults.getCreatedAt())
                    .isPinned(queryResults.isPinned())
                    .build();
        }
    }
}
