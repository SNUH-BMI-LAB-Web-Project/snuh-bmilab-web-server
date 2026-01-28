package com.bmilab.backend.domain.seminar.dto.request;

import com.bmilab.backend.domain.seminar.enums.SeminarLabel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UpdateSeminarRequest(
    @Schema(description = "라벨 (SEMINAR: 세미나, CONFERENCE: 학회)", example = "CONFERENCE")
    @NotNull(message = "라벨은 필수입니다.")
    SeminarLabel label,

    @Schema(description = "제목", example = "대한의료정보학회 춘계학술대회")
    @NotBlank(message = "제목은 필수입니다.")
    String title,

    @Schema(description = "시작일", example = "2025-03-15")
    @NotNull(message = "시작일은 필수입니다.")
    LocalDate startDate,

    @Schema(description = "종료일 (선택)", example = "2025-03-17")
    LocalDate endDate,

    @Schema(description = "기타 메모 (선택)", example = "발표 예정")
    String note
) {}
