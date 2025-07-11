package com.bmilab.backend.domain.user.dto.request;

import com.bmilab.backend.domain.user.enums.Role;
import com.bmilab.backend.domain.user.enums.UserPosition;
import com.bmilab.backend.global.validation.RegExp;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public record RegisterUserRequest(
        @Schema(description = "사용자명", example = "홍길동")
        @NotBlank(message = "사용자명은 필수입니다.")
        @Pattern(regexp = RegExp.NAME_EXPRESSION, message = RegExp.NAME_MESSAGE)
        @Size(max = 10, message = "사용자명은 10자 이하로 입력해주세요.")
        String name,

        @Schema(description = "이메일 주소", example = "hong.gildong@example.com")
        @NotBlank(message = "이메일은 필수입니다.")
        @Pattern(regexp = RegExp.EMAIL_EXPRESSION, message = RegExp.EMAIL_MESSAGE)
        @Size(max = 50, message = "이메일은 50자 이하로 입력해주세요.")
        String email,

        @Schema(description = "비밀번호", example = "SecurePass123!")
        @NotBlank(message = "비밀번호는 필수입니다.")
        @Pattern(regexp = RegExp.PASSWORD_EXPRESSION, message = RegExp.PASSWORD_MESSAGE)
        String password,

        @Schema(description = "기관", example = "융합의학연구실")
        @NotBlank(message = "기관은 필수입니다.")
        @Size(max = 50, message = "기관명은 50자 이하로 입력해주세요.")
        String organization,

        @Schema(description = "부서", example = "개발팀")
        @NotNull(message = "부서는 필수입니다.")
        @Size(max = 20, message = "부서명은 20자 이하로 입력해주세요.")
        String department,

        @Schema(description = "구분 (있으면)", example = "MASTERS_STUDENT")
        @Nullable
        UserPosition position,

        @Schema(description = "서브 소속 (있으면)")
        @Nullable
        List<UserSubAffiliationRequest> subAffiliations,

        @Schema(description = "권한")
        @NotBlank(message = "권한은 필수입니다.")
        Role role,

        @Schema(description = "총 연차 개수", example = "15.0")
        @Nullable
        Double annualLeaveCount,

        @Schema(description = "이미 사용한 연차 개수", example = "3.5")
        @Nullable
        Double usedLeaveCount,

        @Schema(description = "연구 분야 ID 목록")
        @Nullable
        List<Long> categoryIds,

        @Schema(description = "좌석 번호", example = "12-30")
        @Nullable
        String seatNumber,

        @Schema(description = "전화번호", example = "010-1234-5678")
        @Pattern(regexp = RegExp.PHONE_NUMBER_EXPRESSION, message = RegExp.PHONE_NUMBER_MESSAGE)
        String phoneNumber,

        @Schema(description = "학력")
        @Nullable
        @Size(max = 30, message = "학력은 30자 이하로 입력해주세요.")
        List<UserEducationRequest> educations,

        @Schema(description = "입사일", example = "2025-04-01")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @NotNull(message = "입사일은 필수입니다.")
        LocalDate joinedAt
) {
}
