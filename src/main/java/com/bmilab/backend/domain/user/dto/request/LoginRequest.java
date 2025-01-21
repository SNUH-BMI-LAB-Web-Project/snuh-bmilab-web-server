package com.bmilab.backend.domain.user.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
