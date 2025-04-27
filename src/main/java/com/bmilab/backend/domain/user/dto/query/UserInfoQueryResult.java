package com.bmilab.backend.domain.user.dto.query;

import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserInfo;

public record UserInfoQueryResult(
        User user,
        UserInfo userInfo
) {
}
