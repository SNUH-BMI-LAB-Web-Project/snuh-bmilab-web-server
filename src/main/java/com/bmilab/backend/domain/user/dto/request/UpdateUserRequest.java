package com.bmilab.backend.domain.user.dto.request;

import com.bmilab.backend.domain.user.enums.UserPosition;
import com.bmilab.backend.global.validation.RegExp;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateUserRequest(
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

        @Schema(description = "기관", example = "융합의학연구실")
        @NotBlank(message = "기관명은 필수입니다.")
        @Size(max = 50, message = "기관명은 50자 이하로 입력해주세요.")
        String organization,

        @Schema(description = "부서", example = "개발팀")
        @Nullable
        @Size(max = 20, message = "부서명은 20자 이하로 입력해주세요.")
        String department,

        @Schema(description = "구분 (있으면)", example = "MASTERS_STUDENT")
        @Nullable
        UserPosition position,

        @Schema(description = "서브 소속 (있으면)")
        @Nullable
        List<UserSubAffiliationRequest> subAffiliations,

        @Schema(description = "추가된 연구 분야 ID 목록", example = "[1, 2, 3]")
        @Nullable
        List<Long> newCategoryIds,

        @Schema(description = "삭제된 연구 분야 ID 목록", example = "[1, 2, 3]")
        @Nullable
        List<Long> deletedCategoryIds,

        @Schema(description = "전화번호", example = "010-5678-1234")
        @Pattern(regexp = RegExp.PHONE_NUMBER_EXPRESSION,  message = RegExp.PHONE_NUMBER_MESSAGE)
        String phoneNumber,

        @Schema(description = "좌석 번호", example = "14-07")
        @Nullable
        String seatNumber
) {
}
