package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record SearchUserResponse(
        @Schema(description = "검색된 사용자 정보")
        List<UserSummary> users
) {
    public static SearchUserResponse of(List<User> results) {
        return new SearchUserResponse(
                results.stream()
                    .map(UserSummary::from)
                    .toList()
        );
    }
}
