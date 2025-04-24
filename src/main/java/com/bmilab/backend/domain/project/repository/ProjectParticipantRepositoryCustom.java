package com.bmilab.backend.domain.project.repository;

import java.util.List;

public interface ProjectParticipantRepositoryCustom {
    List<Long> findAllUserIdsByProjectIdAndLeader(Long projectId, boolean leader);
}
