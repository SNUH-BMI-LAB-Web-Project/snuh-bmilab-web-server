package com.bmilab.backend.domain.task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record TaskPeriodUpdateRequest(
        @Schema(description = "과제 담당자 ID", example = "1")
        Long managerId,

        @Schema(description = "과제 참여자 ID 목록")
        List<Long> memberIds
) {
}
