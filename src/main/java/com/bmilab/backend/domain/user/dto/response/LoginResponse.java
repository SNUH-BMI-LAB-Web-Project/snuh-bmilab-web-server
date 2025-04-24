package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.enums.Role;

public record LoginResponse(
        String accessToken,
        UserSummary user,
        Role role
) {
    public static LoginResponse of(String accessToken, User user) {
        return new LoginResponse(accessToken, UserSummary.from(user), user.getRole());
    }
}
