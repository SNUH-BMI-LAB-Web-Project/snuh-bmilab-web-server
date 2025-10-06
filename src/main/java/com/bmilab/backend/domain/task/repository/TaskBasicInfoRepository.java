package com.bmilab.backend.domain.task.repository;

import com.bmilab.backend.domain.task.entity.Task;
import com.bmilab.backend.domain.task.entity.TaskBasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskBasicInfoRepository extends JpaRepository<TaskBasicInfo, Long> {
    Optional<TaskBasicInfo> findByTask(Task task);
    void deleteByTask(Task task);
}
