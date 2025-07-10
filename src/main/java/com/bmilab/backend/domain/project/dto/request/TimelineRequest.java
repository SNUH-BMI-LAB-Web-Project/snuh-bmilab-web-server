package com.bmilab.backend.domain.project.dto.request;

import com.bmilab.backend.domain.project.enums.TimelineType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record TimelineRequest(
        @Schema(description = "타임라인 제목", example = "AI 도입 전략 타임라인")
        @NotBlank(message = "타임라인 제목은 필수입니다.")
        @Size(max = 30, message = "타임라인 제목은 최대 30자까지 입력 가능합니다.")
        String title,

        @Schema(description = "타임라인 날짜", example = "2025-05-10")
        @NotNull(message = "타임라인 날짜는 필수입니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @Schema(type = "string", pattern = "HH:mm", example = "14:30")
        @Nullable
        @DateTimeFormat(pattern = "HH:mm")
        LocalTime startTime,

        @Schema(type = "string", pattern = "HH:mm", example = "14:30")
        @Nullable
        @DateTimeFormat(pattern = "HH:mm")
        LocalTime endTime,

        @Schema(description = "미팅 장소", example = "7층 회의실 1")
        @Nullable
        @Size(max = 30, message = "미팅 장소는 최대 30자까지 입력 가능합니다.")
        String meetingPlace,

        @Schema(description = "타임라인 유형 (예: 정기 타임라인, 연구 발표)", example = "정기 타임라인")
        @NotNull(message = "타임라인 유형은 필수입니다.")
        TimelineType type,

        @Schema(description = "타임라인 내용", example = "시장 조사 결과를 바탕으로 AI 기술 도입 로드맵 설정")
        @NotBlank(message = "타임라인 내용은 필수입니다.")
        @Size(max = 200, message = "타임라인 내용은 최대 200자까지 입력 가능합니다.")
        String summary,

        @Schema(description = "타임라인 첨부파일 ID 목록")
        @Nullable
        List<UUID> fileIds
) {
}
