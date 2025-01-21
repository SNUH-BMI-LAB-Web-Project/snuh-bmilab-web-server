package com.bmilab.backend.domain.user.dto.response;

public record LoginResponse(
        String accessToken
) {
    public static LoginResponse of(String accessToken) {
        return new LoginResponse(accessToken);
    }
}
