package com.bmilab.backend.domain.task.repository;

import com.bmilab.backend.domain.task.entity.Task;
import com.bmilab.backend.domain.task.entity.TaskPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskPeriodRepository extends JpaRepository<TaskPeriod, Long> {
    List<TaskPeriod> findByTaskOrderByYearNumberAsc(Task task);
}