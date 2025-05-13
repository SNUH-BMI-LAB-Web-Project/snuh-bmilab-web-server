package com.bmilab.backend.domain.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class ProjectFileId implements Serializable {
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "file_id")
    private UUID fileId;
}
