package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.leave.entity.UserLeave;
import com.bmilab.backend.domain.project.enums.ProjectCategory;
import com.bmilab.backend.domain.user.dto.query.UserDetailQueryResult;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserInfo;
import com.bmilab.backend.domain.user.enums.Role;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import lombok.Builder;

@Builder
public record UserDetail(
        Long userId,
        String email,
        String name,
        String department,
        Role role,
        Double annualLeaveCount,
        Double usedLeaveCount,
        List<ProjectCategory> categories,
        String seatNumber,
        String phoneNumber,
        String comment,
        LocalDate joinedAt
) {
    public static UserDetail from(UserDetailQueryResult queryResult) {
        User user = queryResult.user();
        UserLeave userLeave = queryResult.userLeave();
        UserInfo userInfo = queryResult.userInfo();

        return UserDetail
                .builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .department(user.getDepartment())
                .role(user.getRole())
                .comment(userInfo.getComment())
                .annualLeaveCount(userLeave.getAnnualLeaveCount())
                .usedLeaveCount(userLeave.getUsedLeaveCount())
                .categories(
                        Arrays.stream(userInfo.getCategory().split(","))
                                .map(ProjectCategory::valueOf)
                                .toList()
                )
                .seatNumber(userInfo.getSeatNumber())
                .phoneNumber(userInfo.getPhoneNumber())
                .comment(userInfo.getComment())
                .joinedAt(userInfo.getJoinedAt())
                .build();
    }
}
