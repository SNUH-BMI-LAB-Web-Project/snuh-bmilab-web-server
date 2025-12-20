package com.bmilab.backend.domain.research.presentation.repository;

import com.bmilab.backend.domain.research.presentation.entity.AcademicPresentation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicPresentationRepository extends JpaRepository<AcademicPresentation, Long>, AcademicPresentationRepositoryCustom {
}