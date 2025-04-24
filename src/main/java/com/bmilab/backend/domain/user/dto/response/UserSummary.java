package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserSummary(
        Long userId,
        String email,
        String name,
        String department,
        String profileImageUrl
) {
    public static UserSummary from(User user) {
        return UserSummary
                .builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .department(user.getDepartment())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
