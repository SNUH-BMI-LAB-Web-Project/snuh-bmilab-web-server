package com.bmilab.backend.domain.user.repository;

import com.bmilab.backend.domain.user.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
