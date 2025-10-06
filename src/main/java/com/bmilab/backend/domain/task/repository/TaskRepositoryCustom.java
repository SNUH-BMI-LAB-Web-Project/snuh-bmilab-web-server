package com.bmilab.backend.domain.task.repository;

import com.bmilab.backend.domain.task.entity.Task;
import com.bmilab.backend.domain.task.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskRepositoryCustom {
    Page<Task> findTasksForList(TaskStatus status, String keyword, Pageable pageable);
}