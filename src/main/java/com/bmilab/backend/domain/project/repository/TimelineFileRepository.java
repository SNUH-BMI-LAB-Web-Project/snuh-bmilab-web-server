package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.project.entity.Timeline;
import com.bmilab.backend.domain.project.entity.TimelineFile;
import com.bmilab.backend.domain.project.entity.TimelineFileId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimelineFileRepository extends JpaRepository<TimelineFile, TimelineFileId> {
    Optional<TimelineFile> findByTimelineAndFileInformation(Timeline timeline, FileInformation fileInformation);
}

