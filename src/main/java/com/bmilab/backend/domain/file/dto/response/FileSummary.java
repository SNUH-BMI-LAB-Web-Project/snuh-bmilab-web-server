package com.bmilab.backend.domain.file.dto.response;

import com.bmilab.backend.domain.file.entity.FileInformation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;

@Builder
public record FileSummary(
        @Schema(description = "파일 ID (UUID 형식)", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID fileId,

        @Schema(description = "파일 이름 (확장자 포함)", example = "design_doc.pdf")
        String fileName,

        @Schema(
                description = "S3에 업로드된 파일의 URL",
                example = "https://my-bucket.s3.ap-northeast-2.amazonaws.com/uploads/design_doc.pdf"
        )
        String uploadUrl
) {
    public static FileSummary from(FileInformation fileInformation) {
        return FileSummary.builder()
                .fileId(fileInformation.getId())
                .fileName(fileInformation.getName() + "." + fileInformation.getExtension())
                .uploadUrl(fileInformation.getUploadUrl())
                .build();
    }
}
