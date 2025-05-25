package com.bmilab.backend.domain.file.dto.request;

import com.bmilab.backend.domain.file.enums.FileDomainType;

public record UploadFileRequest(
        String fileName,
        String extension,
        FileDomainType domainType
) {
}
