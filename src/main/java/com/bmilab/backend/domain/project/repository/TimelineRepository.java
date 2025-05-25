package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.entity.Timeline;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimelineRepository extends JpaRepository<Timeline, Long>, TimelineRepositoryCustom {
    List<Timeline> findAllByProjectId(Long projectId);
}
