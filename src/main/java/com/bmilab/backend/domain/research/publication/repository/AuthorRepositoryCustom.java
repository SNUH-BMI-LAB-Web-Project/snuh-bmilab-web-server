package com.bmilab.backend.domain.research.publication.repository;

import com.bmilab.backend.domain.research.publication.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorRepositoryCustom {
    Page<Author> findAllBy(String keyword, Pageable pageable);
}
