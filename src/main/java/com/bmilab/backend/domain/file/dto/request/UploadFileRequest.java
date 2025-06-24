package com.bmilab.backend.domain.file.dto.request;

import com.bmilab.backend.domain.file.enums.FileDomainType;

import java.util.UUID;

public record UploadFileRequest(
        UUID uuid,
        String fileName,
        String extension,
        long size,
        FileDomainType domainType
) {
}
