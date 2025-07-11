package com.bmilab.backend.domain.project.dto.request;

import com.bmilab.backend.global.validation.RegExp;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ExternalProfessorRequest(

        @Schema(description = "외부 교수 이름", example = "황선태")
        @NotBlank(message = "외부 교수 이름은 필수입니다.")
        @Pattern(regexp = RegExp.NAME_EXPRESSION, message = RegExp.NAME_MESSAGE)
        @Size(max = 10, message = "이름은 10자 이하로 입력해주세요.")
        String name,

        @Schema(description = "기관", example = "국민대학교")
        @NotBlank(message = "기관은 필수입니다.")
        @Size(max = 50, message = "기관명은 50자 이하로 입력해주세요.")
        String organization,

        @Schema(description = "부서", example = "소프트웨어학부")
        @Nullable
        @Size(max = 20, message = "부서명은 20자 이하로 입력해주세요.")
        String department,

        @Schema(description = "직책", example = "교수")
        @Nullable
        @Size(max = 20, message = "직책명은 20자 이하로 입력해주세요.")
        String position
) {
}
