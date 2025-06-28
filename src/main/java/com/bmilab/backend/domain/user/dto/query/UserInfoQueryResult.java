package com.bmilab.backend.domain.user.dto.query;

import com.bmilab.backend.domain.projectcategory.entity.ProjectCategory;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserInfo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class UserInfoQueryResult {
    private User user;

    private UserInfo userInfo;

    @Setter
    private List<ProjectCategory> categories;
}