package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.entity.ProjectParticipant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectParticipantRepository extends JpaRepository<ProjectParticipant, Long>, ProjectParticipantRepositoryCustom {
    List<ProjectParticipant> findAllByProjectId(Long projectId);

    void deleteByProjectIdAndUserId(Long projectId, Long userId);
}
