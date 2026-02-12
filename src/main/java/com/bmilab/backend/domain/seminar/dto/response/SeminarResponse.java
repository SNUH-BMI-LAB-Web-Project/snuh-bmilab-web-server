package com.bmilab.backend.domain.seminar.dto.response;

import com.bmilab.backend.domain.seminar.entity.Seminar;
import com.bmilab.backend.domain.seminar.enums.SeminarLabel;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SeminarResponse(
    @Schema(description = "세미나/학회 ID")
    Long id,

    @Schema(description = "라벨")
    SeminarLabel label,

    @Schema(description = "제목")
    String title,

    @Schema(description = "시작일")
    LocalDate startDate,

    @Schema(description = "종료일")
    LocalDate endDate,

    @Schema(description = "기타 메모")
    String note,

    @Schema(description = "등록자 ID")
    Long userId,

    @Schema(description = "등록자 이름")
    String userName,

    @Schema(description = "생성일시")
    LocalDateTime createdAt
) {
    public static SeminarResponse from(Seminar seminar) {
        return new SeminarResponse(
            seminar.getId(),
            seminar.getLabel(),
            seminar.getTitle(),
            seminar.getStartDate(),
            seminar.getEndDate(),
            seminar.getNote(),
            seminar.getUser().getId(),
            seminar.getUser().getName(),
            seminar.getCreatedAt()
        );
    }
}
