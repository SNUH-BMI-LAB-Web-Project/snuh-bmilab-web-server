package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.user.enums.Role;

public record LoginResponse(
        String accessToken,
        Role role
) {
    public static LoginResponse of(String accessToken, Role role) {
        return new LoginResponse(accessToken, role);
    }
}
