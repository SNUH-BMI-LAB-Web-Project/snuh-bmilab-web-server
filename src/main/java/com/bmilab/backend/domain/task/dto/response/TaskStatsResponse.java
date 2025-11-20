package com.bmilab.backend.domain.task.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record TaskStatsResponse(
        @Schema(description = "전체 과제 수", example = "5")
        Long totalCount,

        @Schema(description = "진행 중인 과제 수 (연차수 진행중)", example = "2")
        Long inProgressCount,

        @Schema(description = "제안서 작성 과제 수", example = "1")
        Long proposalWritingCount,

        @Schema(description = "발표 준비 과제 수", example = "2")
        Long presentationPreparingCount
) {
}
