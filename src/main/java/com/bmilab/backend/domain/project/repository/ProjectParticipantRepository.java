package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.entity.ProjectParticipant;
import com.bmilab.backend.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectParticipantRepository extends JpaRepository<ProjectParticipant, Long>, ProjectParticipantRepositoryCustom {
    List<ProjectParticipant> findAllByProjectId(Long projectId);

    void deleteByProjectIdAndUserId(Long projectId, Long userId);

    Optional<ProjectParticipant> findByProjectAndUser(Project project, User user);
}
