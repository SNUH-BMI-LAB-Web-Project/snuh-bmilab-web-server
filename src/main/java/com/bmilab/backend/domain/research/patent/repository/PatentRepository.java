package com.bmilab.backend.domain.research.patent.repository;

import com.bmilab.backend.domain.research.patent.entity.Patent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatentRepository extends JpaRepository<Patent, Long>, PatentRepositoryCustom {
}
