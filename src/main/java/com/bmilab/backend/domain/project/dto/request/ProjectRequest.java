package com.bmilab.backend.domain.project.dto.request;

import com.bmilab.backend.domain.project.enums.ProjectCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

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

        @Schema(description = "대기 상태 여부", example = "false")
        boolean isWaiting,

        @Schema(description = "연구 분야", example = "NLP")
        ProjectCategory category
) {
}
