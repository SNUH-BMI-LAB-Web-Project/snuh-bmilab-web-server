package com.bmilab.backend.domain.research.presentation.repository;

import com.bmilab.backend.domain.research.presentation.dto.response.AcademicPresentationSummaryResponse; // Changed
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AcademicPresentationRepositoryCustom {
    Page<AcademicPresentationSummaryResponse> findAllBy(String keyword, Pageable pageable);
}