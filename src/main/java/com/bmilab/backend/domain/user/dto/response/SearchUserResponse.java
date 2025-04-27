package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.user.entity.User;
import java.util.List;

public record SearchUserResponse(
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
