package com.bmilab.backend.domain.task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record AcknowledgementUpdateRequest(
        @Schema(description = "사사문구", example = "본 연구는 OOO 지원으로 수행되었음")
        String acknowledgementText,

        @Schema(description = "관련정보", example = "과제번호: ...")
        String relatedInfo,

        @Schema(description = "관련링크", example = "https://example.com/link1")
        String relatedLink
) {
}
