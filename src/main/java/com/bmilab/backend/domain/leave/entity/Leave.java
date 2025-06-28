package com.bmilab.backend.domain.leave.entity;

import com.bmilab.backend.domain.leave.enums.LeaveStatus;
import com.bmilab.backend.domain.leave.enums.LeaveType;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "leaves")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Leave extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "leave_status", nullable = false)
    private LeaveStatus status;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "leave_type", nullable = false)
    private LeaveType type;

    @Column(name = "leave_reason")
    private String reason;

    @Column(name = "reject_reason")
    private String rejectReason;

    @Column(name = "leave_count")
    private Double leaveCount;

    @CreatedDate
    @Column(name = "applicated_at", nullable = false)
    private LocalDateTime applicatedAt;

    public void approve() {
        status = LeaveStatus.APPROVED;
    }

    public void reject(String rejectReason) {
        status = LeaveStatus.REJECTED;
        this.rejectReason = rejectReason;
    }

    public boolean isNotPending() {
        return status != LeaveStatus.PENDING;
    }

    public boolean isApproved() {
        return status == LeaveStatus.APPROVED;
    }

    public boolean isAnnualLeave() {
        return type == LeaveType.ANNUAL;
    }

    public int countDaysInYearMonth(YearMonth yearMonth) {
        if (endDate == null) {
            return YearMonth.from(startDate).equals(yearMonth) ? 1 : 0;
        }

        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        // 실제 비교할 시작일과 종료일 설정
        LocalDate effectiveStart = startDate.isBefore(firstDayOfMonth.atStartOfDay()) ? firstDayOfMonth : startDate.toLocalDate();
        LocalDate effectiveEnd = endDate.isAfter(lastDayOfMonth.atStartOfDay()) ? lastDayOfMonth : endDate.toLocalDate();

        // 포함되는 날짜 수 계산
        return effectiveStart.isAfter(effectiveEnd) ? 0 : (int) (effectiveStart.datesUntil(effectiveEnd.plusDays(1)).count());
    }
}
