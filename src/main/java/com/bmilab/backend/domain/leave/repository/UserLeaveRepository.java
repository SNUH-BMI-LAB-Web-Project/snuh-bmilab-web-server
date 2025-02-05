package com.bmilab.backend.domain.leave.repository;

import com.bmilab.backend.domain.leave.entity.UserLeave;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLeaveRepository extends JpaRepository<UserLeave, Long> {
    Optional<UserLeave> findByUserId(long userId);
}
