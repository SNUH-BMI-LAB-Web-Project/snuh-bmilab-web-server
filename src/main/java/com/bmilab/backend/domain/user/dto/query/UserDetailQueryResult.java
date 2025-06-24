package com.bmilab.backend.domain.user.dto.query;

import com.bmilab.backend.domain.leave.entity.UserLeave;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserInfo;

public record UserDetailQueryResult(
        User user,
        UserLeave userLeave,
        UserInfo userInfo
) {
}
