package com.bmilab.backend.domain.task.repository;

import com.bmilab.backend.domain.task.entity.Patent;
import com.bmilab.backend.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskPatentRepository extends JpaRepository<Patent, Long> {
    Optional<Patent> findByTask(Task task);
}
