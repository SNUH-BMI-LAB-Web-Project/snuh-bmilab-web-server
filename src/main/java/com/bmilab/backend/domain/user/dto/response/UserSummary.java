package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record UserSummary(
        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "이메일 주소", example = "hong.gildong@example.com")
        String email,

        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "기관", example = "융합의학연구실")
        String organization,

        @Schema(description = "부서", example = "개발팀")
        String department,

        @Schema(description = "소속 (있으면)", example = "소속")
        String affiliation,

        @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profiles/user1.png")
        String profileImageUrl
) {
    public static UserSummary from(User user) {
        return UserSummary
                .builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .organization(user.getOrganization())
                .department(user.getDepartment())
                .affiliation(user.getAffiliation())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
