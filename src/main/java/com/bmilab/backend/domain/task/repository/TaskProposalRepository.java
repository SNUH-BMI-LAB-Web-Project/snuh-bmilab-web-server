package com.bmilab.backend.domain.task.repository;

import com.bmilab.backend.domain.task.entity.Task;
import com.bmilab.backend.domain.task.entity.TaskProposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskProposalRepository extends JpaRepository<TaskProposal, Long> {
    Optional<TaskProposal> findByTask(Task task);
    void deleteByTask(Task task);
}
