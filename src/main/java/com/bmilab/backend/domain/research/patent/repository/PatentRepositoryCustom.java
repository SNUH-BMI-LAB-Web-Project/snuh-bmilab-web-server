package com.bmilab.backend.domain.research.patent.repository;

import com.bmilab.backend.domain.research.patent.dto.response.PatentSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatentRepositoryCustom {
    Page<PatentSummaryResponse> findAllBy(String keyword, Pageable pageable);
}
