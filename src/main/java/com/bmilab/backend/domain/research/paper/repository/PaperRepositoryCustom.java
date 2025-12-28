package com.bmilab.backend.domain.research.paper.repository;

import com.bmilab.backend.domain.research.paper.entity.Paper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaperRepositoryCustom {
    Page<Paper> findAllBy(String keyword, Pageable pageable);
}
