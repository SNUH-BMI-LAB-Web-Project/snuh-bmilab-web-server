package com.bmilab.backend.domain.task.repository;

import com.bmilab.backend.domain.task.entity.Publication;
import com.bmilab.backend.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
    Optional<Publication> findByTask(Task task);
}
