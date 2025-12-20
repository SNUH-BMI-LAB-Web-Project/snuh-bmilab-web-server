package com.bmilab.backend.domain.research.presentation.repository;

import com.bmilab.backend.domain.research.presentation.entity.AcademicPresentationAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AcademicPresentationAuthorRepository extends JpaRepository<AcademicPresentationAuthor, Long> {
    List<AcademicPresentationAuthor> findAllByAcademicPresentationId(Long academicPresentationId);
    void deleteAllByAcademicPresentationId(Long academicPresentationId);
}
