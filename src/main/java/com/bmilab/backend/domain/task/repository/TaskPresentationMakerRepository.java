package com.bmilab.backend.domain.task.repository;

import com.bmilab.backend.domain.task.entity.TaskPresentation;
import com.bmilab.backend.domain.task.entity.TaskPresentationMaker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskPresentationMakerRepository extends JpaRepository<TaskPresentationMaker, Long> {

    List<TaskPresentationMaker> findByTaskPresentation(TaskPresentation taskPresentation);

    void deleteByTaskPresentation(TaskPresentation taskPresentation);
}
