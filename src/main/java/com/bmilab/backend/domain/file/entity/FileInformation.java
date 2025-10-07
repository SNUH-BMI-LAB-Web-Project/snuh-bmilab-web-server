package com.bmilab.backend.domain.file.entity;

import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "files")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FileInformation extends BaseTimeEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String extension;

    @Column(nullable = false)
    private Long size;

    @Enumerated(EnumType.STRING)
    @Column(name = "domain_type", nullable = false)
    private FileDomainType domainType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "category")
    private String category;

    @Column(name = "upload_url", nullable = false, columnDefinition = "TEXT")
    private String uploadUrl;

    public void updateDomain(FileDomainType domainType, Long entityId) {
        this.domainType = domainType;
        this.entityId = entityId;
    }

    public void updateCategory(String category) {
        this.category = category;
    }

    public void updateUploadUrl(String newUrl) {
        this.uploadUrl = newUrl;
    }
}
