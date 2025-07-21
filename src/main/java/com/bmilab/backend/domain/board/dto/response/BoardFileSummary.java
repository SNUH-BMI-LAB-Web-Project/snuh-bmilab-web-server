package com.bmilab.backend.domain.board.dto.response;

import com.bmilab.backend.domain.board.entity.BoardFile;
import com.bmilab.backend.domain.file.entity.FileInformation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record BoardFileSummary(
        @Schema(description = "파일 ID (UUID 형식)", example = "123e4567-e89b-12d3-a456-426614174000") UUID fileId,

        @Schema(description = "파일 이름 (확장자 포함)", example = "design_doc.pdf")
        String fileName,

        @Schema(
                description = "S3에 업로드된 파일의 URL",
                example = "https://my-bucket.s3.ap-northeast-2.amazonaws.com/uploads/design_doc.pdf"
        )
        String uploadUrl,

        @Schema(description = "파일 사이즈 (bytes 단위)", example = "1023254")
        Long size
) {
    public static BoardFileSummary from(BoardFile boardFile) {
        FileInformation fileInformation = boardFile.getFileInformation();

        return BoardFileSummary.builder()
                .fileId(fileInformation.getId())
                .fileName(fileInformation.getName())
                .uploadUrl(fileInformation.getUploadUrl())
                .build();
    }
}
