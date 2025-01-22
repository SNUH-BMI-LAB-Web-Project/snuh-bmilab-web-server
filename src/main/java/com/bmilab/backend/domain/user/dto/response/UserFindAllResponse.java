package com.bmilab.backend.domain.user.dto.response;

import java.util.List;

public record UserFindAllResponse(
        List<UserDetail> users
) {
    public static UserFindAllResponse of(List<UserDetail> users) {
        return new UserFindAllResponse(users);
    }
}
