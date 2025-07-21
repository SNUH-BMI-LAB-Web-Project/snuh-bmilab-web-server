package com.bmilab.backend.domain.board.dto.response;

import com.bmilab.backend.domain.board.dto.query.GetAllBoardsQueryResult;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

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
            Integer viewCount
    ){
        public static BoardSummary from(GetAllBoardsQueryResult queryResults) {
            return BoardSummary.builder()
                    .boardId(queryResults.getBoardId())
                    .author(queryResults.getAuthor())
                    .boardCategory(queryResults.getBoardCategory())
                    .title(queryResults.getTitle())
                    .viewCount(queryResults.getViewCount())
                    .build();
        }
    }
}
