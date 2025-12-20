package com.bmilab.backend.domain.research.award.repository;

import com.bmilab.backend.domain.research.award.dto.response.AwardSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AwardRepositoryCustom {
    Page<AwardSummaryResponse> findAllBy(String keyword, Pageable pageable);
}
