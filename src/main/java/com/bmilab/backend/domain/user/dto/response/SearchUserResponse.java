package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.user.dto.request.UserSearchConditionRequest;
import com.bmilab.backend.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record SearchUserResponse(
        @Schema(description = "검색된 사용자 정보")
        List<UserSummary> users,

        @Schema(description = "검색 필터 필드", example = "name")
        String filterBy,

        @Schema(description = "검색 필터 값", example = "홍길동")
        String filterValue,

        @Schema(description = "정렬 조건", example = "이름 오름차순")
        String sort

) {
    public static SearchUserResponse of(List<User> users, UserSearchConditionRequest condition) {
        return new SearchUserResponse(
            users.stream()
                    .map(UserSummary::from)
                    .toList(),
                condition.filterBy(),
                condition.filterValue(),
                condition.sort()
        );
    }
}
