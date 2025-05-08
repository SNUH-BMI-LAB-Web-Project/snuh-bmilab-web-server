package com.bmilab.backend.domain.file.repository;

import com.bmilab.backend.domain.file.entity.FileInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileInformationRepository extends JpaRepository<FileInformation, Long> {
}
