package com.bmilab.backend.domain.research.paper.repository;

import com.bmilab.backend.domain.research.paper.dto.response.JournalSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JournalRepositoryCustom {
    Page<JournalSummaryResponse> findAllBy(String keyword, Pageable pageable);
}
