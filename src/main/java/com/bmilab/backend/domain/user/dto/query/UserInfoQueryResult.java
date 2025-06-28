package com.bmilab.backend.domain.user.dto.query;

import com.bmilab.backend.domain.projectcategory.entity.ProjectCategory;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserInfo;
import java.util.List;

public record UserInfoQueryResult(
        User user,
        UserInfo userInfo,
        List<ProjectCategory> categories
) {
}
