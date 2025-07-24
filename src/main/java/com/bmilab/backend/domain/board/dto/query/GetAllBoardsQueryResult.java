package com.bmilab.backend.domain.board.dto.query;

import com.bmilab.backend.domain.board.entity.BoardCategory;
import com.bmilab.backend.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetAllBoardsQueryResult {
    private Long boardId;

    private User author;

    private BoardCategory boardCategory;

    private String title;

    private Integer viewCount;
}

