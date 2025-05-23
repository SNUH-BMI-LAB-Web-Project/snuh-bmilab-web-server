package com.bmilab.backend.domain.project.dto.response;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.project.entity.ProjectFile;
import com.bmilab.backend.domain.project.enums.ProjectFileType;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

public record ProjectFileFindAllResponse(
        List<ProjectFileSummary> files
) {
    @Builder
    public record ProjectFileSummary(
            Long projectId,
            UUID fileId,
            String fileName,
            String uploadUrl,
            ProjectFileType fileType
    ) {
        public static ProjectFileSummary from(ProjectFile projectFile) {
            FileInformation fileInformation = projectFile.getFileInformation();

            return ProjectFileSummary.builder()
                    .projectId(projectFile.getProject().getId())
                    .fileId(fileInformation.getId())
                    .fileName(fileInformation.getName() + "." + fileInformation.getExtension())
                    .uploadUrl(fileInformation.getUploadUrl())
                    .fileType(projectFile.getType())
                    .build();
        }
    }
}
