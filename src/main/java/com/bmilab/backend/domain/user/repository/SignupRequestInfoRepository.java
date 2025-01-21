package com.bmilab.backend.domain.user.repository;

import com.bmilab.backend.domain.user.entity.SignupRequestInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignupRequestInfoRepository extends JpaRepository<SignupRequestInfo, Long> {
}
