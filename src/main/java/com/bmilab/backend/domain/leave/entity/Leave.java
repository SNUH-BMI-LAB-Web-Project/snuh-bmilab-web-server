package com.bmilab.backend.domain.leave.entity;

import com.bmilab.backend.domain.leave.enums.LeaveStatus;
import com.bmilab.backend.domain.leave.enums.LeaveType;
import com.bmilab.backend.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "leaves")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
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
    private Integer leaveCount;

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

    public boolean isPending() {
        return status != LeaveStatus.PENDING;
    }

    public boolean isAnnualLeave() {
        return type == LeaveType.ANNUAL;
    }
}
