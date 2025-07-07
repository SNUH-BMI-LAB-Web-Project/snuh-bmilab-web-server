package com.bmilab.backend.domain.user.repository;

import com.bmilab.backend.domain.user.dto.query.UserCondition;
import com.bmilab.backend.domain.user.dto.query.UserDetailQueryResult;
import com.bmilab.backend.domain.user.dto.query.UserInfoQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<UserDetailQueryResult> findUserDetailsById(Long userId);

    Page<UserInfoQueryResult> searchUserInfos(UserCondition condition, Pageable pageable);
}
