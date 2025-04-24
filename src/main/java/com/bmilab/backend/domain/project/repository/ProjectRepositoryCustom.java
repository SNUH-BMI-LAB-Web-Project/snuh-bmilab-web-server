package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.dto.query.GetAllProjectsQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectRepositoryCustom {
    Page<GetAllProjectsQueryResult> findAllBySearch(String keyword, Pageable pageable);
}
