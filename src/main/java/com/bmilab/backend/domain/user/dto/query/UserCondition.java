package com.bmilab.backend.domain.user.dto.query;

import com.bmilab.backend.domain.user.enums.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCondition {
    private String filterBy;
    private String filterValue;
    private String direction;
    private Integer pageNo = 0;
    private Integer size = 10;
    private String criteria = "createdAt";
    private UserStatus status;
}