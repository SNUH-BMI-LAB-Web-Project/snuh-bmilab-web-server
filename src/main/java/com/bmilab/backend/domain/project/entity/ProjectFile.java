package com.bmilab.backend.domain.project.entity;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.project.enums.ProjectFileType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
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
    @Id
    @Column(name = "file_id", nullable = false)
    private UUID fileId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "file_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FileInformation fileInformation;

    @Enumerated(EnumType.STRING)
    private ProjectFileType type;
}
