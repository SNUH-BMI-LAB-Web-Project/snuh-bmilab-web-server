package com.bmilab.backend.domain.file.repository;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileInformationRepository extends JpaRepository<FileInformation, UUID> {
    List<FileInformation> findAllByDomainTypeAndEntityId(FileDomainType domainType, UUID entityId);
}
