package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.project.enums.ProjectCategory;
import com.bmilab.backend.domain.user.dto.query.UserInfoQueryResult;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
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

            @Schema(description = "소속 (있으면)", example = "소속")
            String affiliation,

            @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profiles/user1.png")
            String profileImageUrl,

            @Schema(description = "연구 분야 목록", example = "[\"NLP\", \"Bioinformatics\"]")
            List<ProjectCategory> categories,

            @Schema(description = "좌석 번호", example = "12-30")
            String seatNumber,

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
                            Arrays.stream(userInfo.getCategory().split(","))
                                    .map(ProjectCategory::valueOf)
                                    .toList()
                    )
                    .seatNumber(userInfo.getSeatNumber())
                    .education(userInfo.getEducation())
                    .build();
        }
    }
}
