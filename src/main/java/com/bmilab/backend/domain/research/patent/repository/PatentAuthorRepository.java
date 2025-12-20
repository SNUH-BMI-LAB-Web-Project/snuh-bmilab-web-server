package com.bmilab.backend.domain.research.patent.repository;

import com.bmilab.backend.domain.research.patent.entity.PatentAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatentAuthorRepository extends JpaRepository<PatentAuthor, Long> {
    List<PatentAuthor> findAllByPatentId(Long patentId);
    void deleteAllByPatentId(Long patentId);
}
