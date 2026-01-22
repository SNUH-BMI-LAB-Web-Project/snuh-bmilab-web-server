package com.bmilab.backend.domain.research.publication.repository;

import com.bmilab.backend.domain.research.publication.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, AuthorRepositoryCustom {
}
