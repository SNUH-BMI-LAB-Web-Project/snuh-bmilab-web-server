package com.bmilab.backend.domain.file.dto.request;

import com.bmilab.backend.domain.file.enums.FileDomainType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UploadFileRequest(
        @Schema(description = "UUID", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull(message = "UUID는 필수입니다.")
        UUID uuid,

        @Schema(description = "파일 이름", example = "글로우업 AI 에디터 개발")
        @NotBlank(message = "파일 이름은 필수입니다.")
        String fileName,

        @Schema(description = "파일 확장자", example = "pdf")
        @NotBlank(message = "파일 확장자는 필수입니다.")
        String extension,

        @Schema(description = "파일 크기 (바이트 단위)", example = "1024")
        @Min(value = 1, message = "파일 크기는 1바이트 이상이어야 합니다.")
        long size
) {
}
