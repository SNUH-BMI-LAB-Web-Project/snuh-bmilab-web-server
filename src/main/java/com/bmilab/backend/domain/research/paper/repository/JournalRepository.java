package com.bmilab.backend.domain.research.paper.repository;

import com.bmilab.backend.domain.research.paper.entity.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalRepository extends JpaRepository<Journal, Long>, JournalRepositoryCustom {
}
