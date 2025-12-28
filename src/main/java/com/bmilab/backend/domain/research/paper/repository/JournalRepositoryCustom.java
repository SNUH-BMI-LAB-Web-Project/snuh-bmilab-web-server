package com.bmilab.backend.domain.research.paper.repository;

import com.bmilab.backend.domain.research.paper.entity.Journal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JournalRepositoryCustom {
    Page<Journal> findAllBy(String keyword, Pageable pageable);
}
