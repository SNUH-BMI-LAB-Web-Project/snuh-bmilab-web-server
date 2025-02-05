package com.bmilab.backend.domain.leave.repository;

import com.bmilab.backend.domain.leave.entity.Leave;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    @Query("SELECT l FROM Leave l"
            + " WHERE (l.startDate BETWEEN :start AND :end)"
            + " OR(l.endDate BETWEEN  :start AND :end)")
    List<Leave> findAllByBetweenDates(LocalDateTime start, LocalDateTime end);

    List<Leave> findAllByUserId(Long userId);
}
