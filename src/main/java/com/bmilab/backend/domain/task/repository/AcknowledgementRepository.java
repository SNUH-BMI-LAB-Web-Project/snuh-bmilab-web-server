package com.bmilab.backend.domain.task.repository;

import com.bmilab.backend.domain.task.entity.Acknowledgement;
import com.bmilab.backend.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AcknowledgementRepository extends JpaRepository<Acknowledgement, Long> {
    Optional<Acknowledgement> findByTask(Task task);
}
