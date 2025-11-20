package com.bmilab.backend.domain.task.entity;

import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "task_agreements")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TaskAgreement extends BaseTimeEntity {

    @Id
    @Column(name = "task_agreement_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "agreement_date")
    private LocalDate agreementDate;

    public void update(LocalDate agreementDate) {
        this.agreementDate = agreementDate;
    }
}
