package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.projectcategory.dto.response.ProjectCategorySummary;
import com.bmilab.backend.domain.user.dto.query.UserInfoQueryResult;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserInfo;
import com.bmilab.backend.domain.user.enums.UserAffiliation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

public record UserFindAllResponse(
        List<UserItem> users,
        int totalPage
) {
    public static UserFindAllResponse of(Page<UserInfoQueryResult> queryResults) {
        return new UserFindAllResponse(
                queryResults.getContent()
                        .stream()
                        .map(UserItem::from)
                        .toList(),
                queryResults.getTotalPages()
        );
    }

    @Builder
    public record UserItem(
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

            @Schema(description = "소속 (있으면)", example = "MASTERS_STUDENT")
            UserAffiliation affiliation,

            @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profiles/user1.png")
            String profileImageUrl,

            @Schema(description = "연구 분야 목록")
            List<ProjectCategorySummary> categories,

            @Schema(description = "좌석 번호", example = "12-30")
            String seatNumber,

            @Schema(description = "전화번호", example = "010-1234-5678")
            String phoneNumber,

            @Schema(description = "학력", example = "국민대학교 소프트웨어학부 재학 중")
            String education
    ) {
        public static UserItem from(UserInfoQueryResult queryResult) {
            User user = queryResult.user();
            UserInfo userInfo = queryResult.userInfo();

            return UserItem.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .organization(user.getOrganization())
                    .department(user.getDepartment())
                    .affiliation(user.getAffiliation())
                    .profileImageUrl(user.getProfileImageUrl())
                    .categories(
                            queryResult.categories()
                                    .stream()
                                    .map(ProjectCategorySummary::from)
                                    .toList()
                    )
                    .seatNumber(userInfo.getSeatNumber())
                    .phoneNumber(userInfo.getPhoneNumber())
                    .education(userInfo.getEducation())
                    .build();
        }
    }
}
