package com.bmilab.backend.domain.research.paper.repository;

import com.bmilab.backend.domain.research.paper.entity.PaperCorrespondingAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaperCorrespondingAuthorRepository extends JpaRepository<PaperCorrespondingAuthor, Long> {
    List<PaperCorrespondingAuthor> findAllByPaperId(Long paperId);
    void deleteAllByPaperId(Long paperId);
}
