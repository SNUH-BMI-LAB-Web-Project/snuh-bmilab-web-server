package com.bmilab.backend.domain.leave.entity;

import com.bmilab.backend.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "user_leaves")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserLeave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", updatable = false, nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "annual_leave_count", nullable = false)
    private Double annualLeaveCount;

    @Column(name = "used_leave_count", nullable = false)
    private Double usedLeaveCount;

    public void useLeave(Double leaveCount, boolean isAnnualLeave) {
        if (isAnnualLeave) {
            annualLeaveCount -= leaveCount;
        }

        usedLeaveCount += leaveCount;
    }

    public void resetLeaveCounts() {
        this.annualLeaveCount = 0.0;
        this.usedLeaveCount = 0.0;
    }

    public void increaseAnnualLeaveCount(Double leaveIncrement) {
        this.annualLeaveCount += leaveIncrement;
    }

    public void updateAnnualLeaveCount(Double annualLeaveCount) {
        this.annualLeaveCount = annualLeaveCount;
    }
}
