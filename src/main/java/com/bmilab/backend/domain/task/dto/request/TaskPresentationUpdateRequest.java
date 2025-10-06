package com.bmilab.backend.domain.task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public record TaskPresentationUpdateRequest(
        @Schema(description = "발표자료 제출 마감일", example = "2025-10-15T18:00:00")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime presentationDeadline,

        @Schema(description = "발표자료 제작 연구원 ID 목록", example = "[101, 102, 103]")
        List<Long> presentationMakerIds,

        @Schema(description = "발표평가일시", example = "2025-10-20T14:00:00")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime presentationDate,

        @Schema(description = "발표자 이름", example = "김철수")
        String presenter,

        @Schema(description = "배석 가능 인원", example = "5")
        Integer attendeeLimit,

        @Schema(description = "실제 배석자 명단 (콤마로 구분)", example = "홍길동, 이영희, 박민수")
        String attendees,

        @Schema(description = "발표평가장 위치", example = "서울대병원 본관 3층 대회의실")
        String presentationLocation
) {
}
