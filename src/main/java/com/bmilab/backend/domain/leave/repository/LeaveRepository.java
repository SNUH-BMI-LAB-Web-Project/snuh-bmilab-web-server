package com.bmilab.backend.domain.leave.repository;

import com.bmilab.backend.domain.leave.entity.Leave;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.bmilab.backend.domain.leave.enums.LeaveStatus;
import com.bmilab.backend.domain.leave.enums.LeaveType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    @Query("SELECT l FROM Leave l"
            + " WHERE (l.startDate BETWEEN :start AND :end)"
            + " OR(l.endDate BETWEEN  :start AND :end)")
    List<Leave> findAllByBetweenDates(LocalDate start, LocalDate end);

    @Query("SELECT l FROM Leave l"
            + " WHERE ((l.startDate BETWEEN :start AND :end) OR (l.endDate BETWEEN  :start AND :end))"
            + " AND l.status = :status")
    List<Leave> findAllByBetweenDatesAndStatus(LocalDate start, LocalDate end, LeaveStatus status);

    Page<Leave> findAllByUserId(Long userId, Pageable pageable);

    List<Leave> findAllByUserId(Long userId);

    Page<Leave> findAllByStatus(LeaveStatus status, Pageable pageable);

    List<Leave> findAllByStatus(LeaveStatus status);
}
