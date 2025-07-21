package com.bmilab.backend.domain.board.repository;

import com.bmilab.backend.domain.board.dto.query.GetAllBoardsQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<GetAllBoardsQueryResult> findAllByFiltering(
            Long boardId,
            String keyword,
            String category,
            Pageable pageable
    );
}
