package com.bmilab.backend.domain.research.patent.repository;

import com.bmilab.backend.domain.research.patent.entity.Patent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatentRepository extends JpaRepository<Patent, Long>, PatentRepositoryCustom {
    List<Patent> findAllByTaskId(Long taskId);
}
