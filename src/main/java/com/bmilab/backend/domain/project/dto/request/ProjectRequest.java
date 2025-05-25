package com.bmilab.backend.domain.project.dto.request;

import com.bmilab.backend.domain.project.enums.ProjectCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ProjectRequest(
        @Schema(description = "연구 제목", example = "글로우업 AI 에디터 개발")
        String title,

        @Schema(description = "연구 설명", example = "Gemini 기반 AI 텍스트 작성 툴 백엔드 구현")
        String content,

        @Schema(description = "연구 책임자의 사용자 ID 리스트", example = "[1, 2]")
        List<Long> leaderIds,

        @Schema(description = "연구 참여자의 사용자 ID 리스트", example = "[3, 4, 5]")
        List<Long> participantIds,

        @Schema(description = "연구 시작일", example = "2025-05-01")
        LocalDate startDate,

        @Schema(description = "연구 종료일 (있으면)", example = "2025-06-30")
        LocalDate endDate,

        @Schema(description = "PI", example = "김광수")
        String pi,

        @Schema(description = "실무 교수", example = "김광수")
        String practicalProfessor,

        @Schema(description = "IRB 번호 (있으면)", example = "IRB-DSEB-...")
        String irbId,

        @Schema(description = "DRB 번호 (있으면)", example = "DRB-DSEB-...")
        String drbId,

        @Schema(description = "(새로 추가된 파일의 ID만) IRB 파일 ID 리스트", example = "[dfaef-afaefaef-aefaefae-..., efaeaf-aefafea-aefaef..]")
        List<UUID> irbFileIds,

        @Schema(description = "(새로 추가된 파일의 ID만) DRB 파일 ID 리스트", example = "[dfaef-afaefaef-aefaefae-..., dfaef-afaefaef-aefaefae-..., dfaef-afaefaef-aefaefae-...]")
        List<UUID> drbFileIds,

        @Schema(description  = "(새로 추가된 파일의 ID만) 일반 첨부파일 ID 리스트", example = "[dfaef-afaefaef-aefaefae-..., dfaef-afaefaef-aefaefae-..., dfaef-afaefaef-aefaefae-...]")
        List<UUID> fileIds,

        @Schema(description = "대기 상태 여부", example = "false")
        boolean isWaiting,

        @Schema(description = "연구 분야", example = "NLP")
        ProjectCategory category
) {
}
