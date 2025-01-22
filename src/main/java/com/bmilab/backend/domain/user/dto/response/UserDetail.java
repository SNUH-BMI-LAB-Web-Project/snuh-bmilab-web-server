package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.enums.Role;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record UserDetail(
        Long userId,
        String email,
        String name,
        String department,
        Integer leaveCount,
        String role,
        String comment,
        LocalDateTime createdAt
) {
    public static UserDetail from(User user) {
        return UserDetail
                .builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .department(user.getDepartment())
                .leaveCount(user.getLeaveCount())
                .role(user.getRole().name())
                .comment(user.getComment())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
