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

    @Query("SELECT l FROM Leave l " + "WHERE ( (l.endDate IS NULL AND l.startDate BETWEEN :start AND :end) " +
            "     OR (l.endDate IS NOT NULL AND l.startDate <= :end AND l.endDate >= :start) )")
    List<Leave> findAllByBetweenDates(LocalDate start, LocalDate end);

    @Query("SELECT l FROM Leave l " + "WHERE l.status = :status AND " +
            "( (l.endDate IS NULL AND l.startDate BETWEEN :start AND :end) " +
            "OR (l.endDate IS NOT NULL AND l.startDate <= :end AND l.endDate >= :start) )")
    List<Leave> findAllByBetweenDatesAndStatus(LocalDate start, LocalDate end, LeaveStatus status);

    // 금주 휴가자 알림용 - 오늘 이후 종료되는 휴가만 조회 (user fetch join)
    @Query("SELECT l FROM Leave l JOIN FETCH l.user " +
            "WHERE l.status = :status AND " +
            "( (l.endDate IS NULL AND l.startDate BETWEEN :today AND :endOfWeek) " +
            "OR (l.endDate IS NOT NULL AND l.startDate <= :endOfWeek AND l.endDate >= :today) ) " +
            "ORDER BY l.startDate ASC, l.user.name ASC")
    List<Leave> findWeeklyLeavesEndingAfterToday(LocalDate today, LocalDate endOfWeek, LeaveStatus status);

    Page<Leave> findAllByUserId(Long userId, Pageable pageable);

    List<Leave> findAllByUserId(Long userId);

    Page<Leave> findAllByStatus(LeaveStatus status, Pageable pageable);

    List<Leave> findAllByStatus(LeaveStatus status);
}
