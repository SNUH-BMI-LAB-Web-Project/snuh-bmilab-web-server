package com.bmilab.backend.domain.project.entity;

import com.bmilab.backend.domain.file.entity.FileInformation;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "project_files")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TimelineFile {
    @EmbeddedId
    private TimelineFileId id;

    @MapsId("timelineId")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Timeline timeline;

    @MapsId("fileId")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FileInformation fileInformation;
}
