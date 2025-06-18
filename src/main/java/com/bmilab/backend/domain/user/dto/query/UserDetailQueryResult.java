package com.bmilab.backend.domain.user.dto.query;

import com.bmilab.backend.domain.leave.entity.Leave;
import com.bmilab.backend.domain.leave.entity.UserLeave;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserInfo;
import java.util.List;

public record UserDetailQueryResult(
        User user,
        UserLeave userLeave,
        UserInfo userInfo,
        List<Leave> leaves
) {
}
