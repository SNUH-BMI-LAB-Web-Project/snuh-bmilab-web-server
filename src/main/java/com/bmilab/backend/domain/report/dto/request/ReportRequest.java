package com.bmilab.backend.domain.report.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ReportRequest(
        @Schema(description = "프로젝트 아이디", example = "1.0")
        @NotNull(message = "프로젝트 ID는 필수입니다.")
        Long projectId,

        @Schema(description = "생성 일자", example = "2024-12-07")
        @NotNull(message = "생성 일자는 필수입니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @Schema(description = "업무 내용", example = "팀 회의 참석 후, 프로젝트 진행 상황 점검")
        @NotNull(message = "업무 내용은 필수입니다.")
        String content,

        @Schema(description  = "일반 첨부파일 ID 리스트", example = "[\"dfaef-afaefaef-aefaefae-...\", \"dfaef-afaefaef-aefaefae-..., dfaef-afaefaef-aefaefae-...\"]")
        @Nullable
        List<UUID> fileIds
) {
}
