package com.bmilab.backend.domain.research.paper.repository;

import com.bmilab.backend.domain.research.paper.entity.PaperAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaperAuthorRepository extends JpaRepository<PaperAuthor, Long> {
    List<PaperAuthor> findAllByPaperId(Long paperId);
    void deleteAllByPaperId(Long paperId);
}
