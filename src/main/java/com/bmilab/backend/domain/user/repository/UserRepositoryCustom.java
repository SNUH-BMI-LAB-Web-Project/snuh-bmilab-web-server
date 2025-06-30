package com.bmilab.backend.domain.user.repository;

import com.bmilab.backend.domain.user.dto.query.UserDetailQueryResult;
import com.bmilab.backend.domain.user.dto.query.UserInfoQueryResult;
import com.bmilab.backend.domain.user.dto.request.UserSearchConditionRequest;
import com.bmilab.backend.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<UserDetailQueryResult> findUserDetailsById(Long userId);

    Page<UserInfoQueryResult> findAllUserInfosPagination(Pageable pageable);

    List<User> searchUsersByCondition(UserSearchConditionRequest condition);
}
