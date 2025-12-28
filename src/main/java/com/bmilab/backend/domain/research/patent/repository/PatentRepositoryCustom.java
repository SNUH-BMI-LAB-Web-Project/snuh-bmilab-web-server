package com.bmilab.backend.domain.research.patent.repository;

import com.bmilab.backend.domain.research.patent.entity.Patent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatentRepositoryCustom {
    Page<Patent> findAllBy(String keyword, Pageable pageable);
}
