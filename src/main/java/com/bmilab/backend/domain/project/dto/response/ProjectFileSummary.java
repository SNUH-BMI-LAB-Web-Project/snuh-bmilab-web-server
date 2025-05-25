package com.bmilab.backend.domain.project.dto.response;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.project.entity.ProjectFile;
import com.bmilab.backend.domain.project.enums.ProjectFileType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ProjectFileSummary(
        @Schema(description = "파일 ID (UUID 형식)", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID fileId,

        @Schema(description = "파일 이름 (확장자 포함)", example = "design_doc.pdf")
        String fileName,

        @Schema(
                description = "S3에 업로드된 파일의 URL",
                example = "https://my-bucket.s3.ap-northeast-2.amazonaws.com/uploads/design_doc.pdf"
        )
        String uploadUrl,

        @Schema(description = "프로젝트 파일 유형")
        ProjectFileType fileType
) {
    public static ProjectFileSummary from(ProjectFile projectFile) {
        FileInformation fileInformation = projectFile.getFileInformation();

        return ProjectFileSummary.builder()
                .fileId(fileInformation.getId())
                .fileName(fileInformation.getName() + "." + fileInformation.getExtension())
                .uploadUrl(fileInformation.getUploadUrl())
                .fileType(projectFile.getType())
                .build();
    }

    public static ProjectFileSummary from(FileInformation fileInformation, ProjectFileType fileType) {
        return ProjectFileSummary.builder()
                .fileId(fileInformation.getId())
                .fileName(fileInformation.getName() + "." + fileInformation.getExtension())
                .uploadUrl(fileInformation.getUploadUrl())
                .fileType(fileType)
                .build();
    }
}