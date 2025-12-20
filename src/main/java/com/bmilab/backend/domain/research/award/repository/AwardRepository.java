package com.bmilab.backend.domain.research.award.repository;

import com.bmilab.backend.domain.research.award.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwardRepository extends JpaRepository<Award, Long>, AwardRepositoryCustom {
}
