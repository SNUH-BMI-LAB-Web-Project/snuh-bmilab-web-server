package com.bmilab.backend.domain.research.presentation.repository;

import com.bmilab.backend.domain.research.presentation.entity.AcademicPresentation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AcademicPresentationRepository extends JpaRepository<AcademicPresentation, Long>, AcademicPresentationRepositoryCustom {
    List<AcademicPresentation> findAllByTaskId(Long taskId);
}