package com.bmilab.backend.domain.research.award.repository;

import com.bmilab.backend.domain.research.award.entity.Award;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AwardRepositoryCustom {
    Page<Award> findAllBy(String keyword, Pageable pageable);
}
