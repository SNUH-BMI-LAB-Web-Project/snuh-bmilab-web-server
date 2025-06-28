package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.entity.ExternalProfessor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalProfessorRepository extends JpaRepository<ExternalProfessor, Long>, ExternalProfessorRepositoryCustom {
}
