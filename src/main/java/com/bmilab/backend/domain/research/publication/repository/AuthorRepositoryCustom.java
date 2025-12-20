package com.bmilab.backend.domain.research.publication.repository;

import com.bmilab.backend.domain.research.publication.dto.response.AuthorSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorRepositoryCustom {
    Page<AuthorSummaryResponse> findAllBy(String keyword, Pageable pageable);
}
