package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.leave.entity.UserLeave;
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
        Role role,
        String comment,
        Double annualLeaveCount,
        Double usedLeaveCount,
        LocalDateTime joinedAt
) {
    public static UserDetail from(User user, UserLeave userLeave) {
        return UserDetail
                .builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .department(user.getDepartment())
                .role(user.getRole())
                .comment(user.getComment())
                .annualLeaveCount(userLeave.getAnnualLeaveCount())
                .usedLeaveCount(userLeave.getUsedLeaveCount())
                .joinedAt(user.getCreatedAt())
                .build();
    }
}
