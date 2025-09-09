package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.entity.Timeline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimelineRepository extends JpaRepository<Timeline, Long>, TimelineRepositoryCustom {
    List<Timeline> findAllByProjectId(Long projectId);
}
