package com.bmilab.backend.domain.project.entity;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.project.enums.ProjectFileType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "project_files")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProjectFile {
    @EmbeddedId
    private ProjectFileId id;

    @MapsId("projectId")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;

    @MapsId("fileId")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FileInformation fileInformation;

    @Enumerated(EnumType.STRING)
    private ProjectFileType type;
}
