package com.bmilab.backend.domain.projectcategory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectCategoryRequest(
        @Schema(description = "연구 분야 이름", example = "Artificial Intelligence")
        @NotBlank(message = "연구 분야 이름은 필수입니다.")
        @Size(max = 30, message = "연구 분야 이름은 30자 이하로 입력해주세요.")
        String name
) {
}
