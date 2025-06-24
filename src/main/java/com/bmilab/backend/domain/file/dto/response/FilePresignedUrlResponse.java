package com.bmilab.backend.domain.file.dto.response;

import java.util.UUID;

public record FilePresignedUrlResponse(
        UUID uuid,
        String presignedUrl
) {
}
