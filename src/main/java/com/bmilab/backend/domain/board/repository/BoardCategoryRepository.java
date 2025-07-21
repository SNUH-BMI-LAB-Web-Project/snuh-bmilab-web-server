package com.bmilab.backend.domain.board.repository;

import com.bmilab.backend.domain.board.entity.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {
}
