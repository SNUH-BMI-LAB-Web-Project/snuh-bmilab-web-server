package com.bmilab.backend.domain.user.repository;

import com.bmilab.backend.domain.user.dto.query.UserDetailQueryResult;
import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<UserDetailQueryResult> findUserDetailsById(Long userId);
}
