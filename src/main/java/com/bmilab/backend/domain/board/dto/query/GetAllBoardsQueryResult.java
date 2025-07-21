package com.bmilab.backend.domain.board.dto.query;

import com.bmilab.backend.domain.board.dto.response.BoardCategorySummary;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetAllBoardsQueryResult {
    private Long boardId;

    private UserSummary author;

    private BoardCategorySummary boardCategory;

    private String title;

    private Integer viewCount;
}
