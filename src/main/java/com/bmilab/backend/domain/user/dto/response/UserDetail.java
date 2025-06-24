package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.leave.entity.UserLeave;
import com.bmilab.backend.domain.project.enums.ProjectCategory;
import com.bmilab.backend.domain.user.dto.query.UserDetailQueryResult;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserEducation;
import com.bmilab.backend.domain.user.entity.UserInfo;
import com.bmilab.backend.domain.user.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import lombok.Builder;

@Builder
public record UserDetail(
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

        @Schema(description = "사용자 역할", example = "USER")
        Role role,

        @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profiles/user1.png")
        String profileImageUrl,

        @Schema(description = "총 연차 수", example = "15.0")
        Double annualLeaveCount,

        @Schema(description = "사용한 연차 수", example = "3.5")
        Double usedLeaveCount,

        @Schema(description = "연구 분야 목록", example = "[\"NLP\", \"Bioinformatics\"]")
        List<ProjectCategory> categories,

        @Schema(description = "좌석 번호", example = "12-30")
        String seatNumber,

        @Schema(description = "전화번호", example = "010-1234-5678")
        String phoneNumber,

        @Schema(description = "학력", example = "국민대학교 소프트웨어학부 재학 중")
        List<UserEducationSummary> educations,

        @Schema(description = "비고 또는 한 줄 소개", example = "딥러닝과 커피를 사랑하는 개발자")
        String comment,

        @Schema(description = "입사일", example = "2023-03-01")
        LocalDate joinedAt
) {
    public static UserDetail from(UserDetailQueryResult queryResult, List<UserEducation> educations, boolean includeComment) {
        User user = queryResult.user();
        UserLeave userLeave = queryResult.userLeave();
        UserInfo userInfo = queryResult.userInfo();

        return UserDetail
                .builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .organization(user.getOrganization())
                .department(user.getDepartment())
                .affiliation(user.getAffiliation())
                .role(user.getRole())
                .profileImageUrl(user.getProfileImageUrl())
                .comment((includeComment) ? userInfo.getComment() : null)
                .annualLeaveCount(userLeave.getAnnualLeaveCount())
                .usedLeaveCount(userLeave.getUsedLeaveCount())
                .categories(
                        Arrays.stream(userInfo.getCategory().split(","))
                                .map(ProjectCategory::valueOf)
                                .toList()
                )
                .seatNumber(userInfo.getSeatNumber())
                .phoneNumber(userInfo.getPhoneNumber())
                .educations(educations.stream().map(UserEducationSummary::from).toList())
                .comment(userInfo.getComment())
                .joinedAt(userInfo.getJoinedAt())
                .build();
    }
}
