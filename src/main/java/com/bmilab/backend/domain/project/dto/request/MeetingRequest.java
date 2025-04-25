package com.bmilab.backend.domain.project.dto.request;

import com.bmilab.backend.domain.project.enums.MeetingType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;

public record MeetingRequest(
        @Schema(description = "회의 제목", example = "AI 도입 전략 회의")
        String title,

        @Schema(description = "회의 날짜", example = "2025-05-10")
        LocalDate date,

        @Schema(type = "string", pattern = "HH:mm", example = "14:30")
        LocalTime startTime,
        
        @Schema(type = "string", pattern = "HH:mm", example = "14:30")
        LocalTime endTime,

        @Schema(description = "회의 유형 (예: ONLINE, OFFLINE)", example = "ONLINE")
        MeetingType type,

        @Schema(description = "회의 요약", example = "AI 도입의 전략적 방향성과 단계 논의")
        String summary,

        @Schema(description = "회의 내용", example = "시장 조사 결과를 바탕으로 AI 기술 도입 로드맵 설정")
        String content
) {
}
