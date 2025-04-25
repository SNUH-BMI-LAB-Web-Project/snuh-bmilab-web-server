package com.bmilab.backend.domain.project.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProjectFileRequest(
        @Schema(description = "프로젝트 첨부파일 URL", example = "https://www.test.com/test.jpg")
        String fileUrl
) {
}
