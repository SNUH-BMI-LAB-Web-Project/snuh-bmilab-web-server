package com.bmilab.backend.domain.file.entity;

import com.bmilab.backend.domain.file.enums.FileDomainType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "files")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class FileInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String extension;

    @Enumerated(EnumType.STRING)
    @Column(name = "domain_type", nullable = false)
    private FileDomainType domainType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "upload_url", nullable = false)
    private String uploadUrl;

    public void updateDomain(FileDomainType domainType, Long entityId) {
        this.domainType = domainType;
        this.entityId = entityId;
    }
}
