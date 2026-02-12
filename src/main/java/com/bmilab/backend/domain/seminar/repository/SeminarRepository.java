package com.bmilab.backend.domain.seminar.repository;

import com.bmilab.backend.domain.seminar.entity.Seminar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeminarRepository extends JpaRepository<Seminar, Long>, SeminarRepositoryCustom {
}
