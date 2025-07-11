package com.bmilab.backend.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserSubAffiliationRequest(
        @Schema(description = "기관", example = "국민대학교")
        @NotBlank
        @Size(max = 50)
        String organization,

        @Schema(description = "부서", example = "소프트웨어학부")
        @Nullable
        @Size(max = 20)
        String department,

        @Schema(description = "구분", example = "학부생")
        @Nullable
        @Size(max = 20)
        String position
) {
}