package com.bmilab.backend.domain.user.dto.response;

import java.util.List;

public record UserFindAllResponse(
        List<UserSummary> users
) {
    public static UserFindAllResponse of(List<UserSummary> users) {
        return new UserFindAllResponse(users);
    }
}
