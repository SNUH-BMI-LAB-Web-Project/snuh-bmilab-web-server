package com.bmilab.backend.domain.project.dto.request;

import com.bmilab.backend.domain.project.dto.ExternalProfessorSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ProjectRequest(
        @Schema(description = "연구 제목", example = "글로우업 AI 에디터 개발")
        @NotBlank(message = "연구 제목은 필수입니다.")
        String title,

        @Schema(description = "연구 설명", example = "Gemini 기반 AI 텍스트 작성 툴 백엔드 구현")
        @NotNull(message = "연구 설명은 필수입니다.")
        String content,

        @Schema(description = "연구 책임자의 사용자 ID 리스트", example = "[1, 2]")
        @NotNull(message = "연구 책임자 ID 리스트는 필수입니다.")
        List<Long> leaderIds,

        @Schema(description = "연구 참여자의 사용자 ID 리스트", example = "[3, 4, 5]")
        @Nullable
        List<Long> participantIds,

        @Schema(description = "연구 시작일", example = "2025-05-01")
        @NotNull(message = "연구 시작일은 필수입니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd") // LocalDate에 맞는 포맷
        LocalDate startDate,

        @Schema(description = "연구 종료일 (있으면)", example = "2025-06-30")
        @Nullable
        @DateTimeFormat(pattern = "yyyy-MM-dd") // LocalDate에 맞는 포맷
        LocalDate endDate,

        @Schema(description = "PI")
        @Nullable
        List<ExternalProfessorRequest> piList,

        @Schema(description = "실무 교수")
        @Nullable
        List<ExternalProfessorRequest> practicalProfessors,

        @Schema(description = "IRB 번호 (있으면)", example = "IRB-DSEB-...")
        @Nullable
        String irbId,

        @Schema(description = "DRB 번호 (있으면)", example = "DRB-DSEB-...")
        @Nullable
        String drbId,

        @Schema(description = "(새로 추가된 파일의 ID만) IRB 파일 ID 리스트", example = "[\"dfaef-afaefaef-aefaefae-...\", \"efaeaf-aefafea-aefaef..\"]")
        @Nullable
        List<UUID> irbFileIds,

        @Schema(description = "(새로 추가된 파일의 ID만) DRB 파일 ID 리스트", example = "[\"dfaef-afaefaef-aefaefae-...\", \"dfaef-afaefaef-aefaefae-...\", \"dfaef-afaefaef-aefaefae-...\"]")
        @Nullable
        List<UUID> drbFileIds,

        @Schema(description  = "(새로 추가된 파일의 ID만) 일반 첨부파일 ID 리스트", example = "[\"dfaef-afaefaef-aefaefae-...\", \"dfaef-afaefaef-aefaefae-..., dfaef-afaefaef-aefaefae-...\"]")
        @Nullable
        List<UUID> fileIds,

        @Schema(description = "대기 상태 여부", example = "false")
        @NotNull(message = "대기 상태 여부는 필수입니다.")
        boolean isWaiting,

        @Schema(description = "연구 분야 아이디", example = "1")
        @NotNull(message = "연구 분야 ID는 필수입니다.")
        Long categoryId,

        @Schema(description = "연구 비공개 여부")
        @NotNull(message = "연구 비공개 여부는 필수입니다.")
        boolean isPrivate
) {
}
