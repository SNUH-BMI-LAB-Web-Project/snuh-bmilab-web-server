package com.bmilab.backend.domain.task.repository;

import com.bmilab.backend.domain.task.entity.TaskPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskPeriodRepository extends JpaRepository<TaskPeriod, Long> {
}