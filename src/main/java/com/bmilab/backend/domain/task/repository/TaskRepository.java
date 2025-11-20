package com.bmilab.backend.domain.task.repository;

import com.bmilab.backend.domain.task.entity.Task;
import com.bmilab.backend.domain.task.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {

    @Query("SELECT COUNT(t) FROM Task t")
    Long countAllTasks();

    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = :status")
    Long countByStatus(@Param("status") TaskStatus status);

    boolean existsByResearchTaskNumber(String researchTaskNumber);
}