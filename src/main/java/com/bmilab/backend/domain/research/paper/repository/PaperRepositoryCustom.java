package com.bmilab.backend.domain.research.paper.repository;

import com.bmilab.backend.domain.research.paper.dto.response.PaperSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaperRepositoryCustom {
    Page<PaperSummaryResponse> findAllBy(String keyword, Pageable pageable);
}
