package com.bmilab.backend.domain.task.repository;

import com.bmilab.backend.domain.task.entity.Conference;
import com.bmilab.backend.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    Optional<Conference> findByTask(Task task);
}
