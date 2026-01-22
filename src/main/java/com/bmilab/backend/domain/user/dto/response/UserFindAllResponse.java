package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.projectcategory.dto.response.ProjectCategorySummary;
import com.bmilab.backend.domain.user.dto.query.UserCondition;
import com.bmilab.backend.domain.user.dto.query.UserInfoQueryResult;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserInfo;
import com.bmilab.backend.domain.user.enums.UserPosition;
import com.bmilab.backend.domain.user.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public record UserFindAllResponse(
        List<UserItem> users,

        @Schema(description = "검색 필터 필드", example = "name")
        String filterBy,

        @Schema(description = "검색 필터 값", example = "홍길동")
        String filterValue,

        @Schema(description = "필터 내 정렬 기준", example = "이름 오름차순")
        String sort,

        @Schema(description = "전체 페이지 수", example = "5")
        int totalPage
) {
    public static UserFindAllResponse of(UserCondition condition, Page<UserInfoQueryResult> queryResults) {
        return new UserFindAllResponse(
                queryResults.getContent()
                        .stream()
                        .map(UserItem::from)
                        .toList(),
                condition.getFilterBy(),
                condition.getFilterValue(),
                condition.getDirection(),
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

            @Schema(description = "구분 (있으면)", example = "MASTERS_STUDENT")
            UserPosition position,

            @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profiles/user1.png")
            String profileImageUrl,

            @Schema(description = "연구 분야 목록")
            List<ProjectCategorySummary> categories,

            @Schema(description = "좌석 번호", example = "12-30")
            String seatNumber,

            @Schema(description = "전화번호", example = "010-1234-5678")
            String phoneNumber,

            @Schema(description = "학력", example = "국민대학교 소프트웨어학부 재학 중")
            String education,

            @Schema(description = "입사일", example = "2023-03-01")
            LocalDate joinedAt,

            @Schema(description = "상태", example = "ACTIVE")
            UserStatus status
    ) {
        public static UserItem from(UserInfoQueryResult queryResult) {
            User user = queryResult.getUser();
            UserInfo userInfo = queryResult.getUserInfo();

            return UserItem.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .organization(user.getOrganization())
                    .department(user.getDepartment())
                    .position(user.getPosition())
                    .profileImageUrl(user.getProfileImageUrl())
                    .categories(
                            queryResult.getCategories()
                                    .stream()
                                    .map(ProjectCategorySummary::from)
                                    .toList()
                    )
                    .seatNumber(userInfo != null ? userInfo.getSeatNumber() : null)
                    .phoneNumber(userInfo != null ? userInfo.getPhoneNumber() : null)
                    .education(userInfo != null ? userInfo.getEducation() : null)
                    .joinedAt(userInfo != null ? userInfo.getJoinedAt() : null)
                    .status(user.getStatus())
                    .build();
        }
    }
}
