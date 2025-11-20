package com.bmilab.backend.domain.task.repository;

import com.bmilab.backend.domain.task.entity.Task;
import com.bmilab.backend.domain.task.entity.TaskAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskAgreementRepository extends JpaRepository<TaskAgreement, Long> {
    Optional<TaskAgreement> findByTask(Task task);
    void deleteByTask(Task task);
}
