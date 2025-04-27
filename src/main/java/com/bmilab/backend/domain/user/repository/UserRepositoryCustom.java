package com.bmilab.backend.domain.user.repository;

import com.bmilab.backend.domain.user.dto.query.UserDetailQueryResult;
import com.bmilab.backend.domain.user.dto.query.UserInfoQueryResult;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Optional<UserDetailQueryResult> findUserDetailsById(Long userId);

    Page<UserInfoQueryResult> findAllUserInfosPagination(Pageable pageable);
}
