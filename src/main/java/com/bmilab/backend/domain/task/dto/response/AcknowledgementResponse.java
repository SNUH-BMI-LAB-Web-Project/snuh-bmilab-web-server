package com.bmilab.backend.domain.task.dto.response;

import com.bmilab.backend.domain.task.entity.Acknowledgement;
import io.swagger.v3.oas.annotations.media.Schema;

public record AcknowledgementResponse(
        @Schema(description = "사사표기 ID")
        Long id,

        @Schema(description = "사사문구")
        String acknowledgementText,

        @Schema(description = "관련정보")
        String relatedInfo,

        @Schema(description = "관련링크")
        String relatedLink
) {
    public static AcknowledgementResponse from(Acknowledgement acknowledgement) {
        if (acknowledgement == null) {
            return null;
        }
        return new AcknowledgementResponse(
                acknowledgement.getId(),
                acknowledgement.getAcknowledgementText(),
                acknowledgement.getRelatedInfo(),
                acknowledgement.getRelatedLink()
        );
    }
}
