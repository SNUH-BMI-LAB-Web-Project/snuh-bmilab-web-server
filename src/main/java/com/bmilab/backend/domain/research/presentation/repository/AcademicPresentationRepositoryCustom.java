package com.bmilab.backend.domain.research.presentation.repository;

import com.bmilab.backend.domain.research.presentation.entity.AcademicPresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AcademicPresentationRepositoryCustom {
    Page<AcademicPresentation> findAllBy(String keyword, Pageable pageable);
}