package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.entity.Meeting;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findAllByProjectId(Long projectId);
}
