package com.bmilab.backend.domain.task.repository;

import com.bmilab.backend.domain.task.entity.Task;
import com.bmilab.backend.domain.task.entity.TaskPresentation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskPresentationRepository extends JpaRepository<TaskPresentation, Long> {
    Optional<TaskPresentation> findByTask(Task task);
    void deleteByTask(Task task);
}
