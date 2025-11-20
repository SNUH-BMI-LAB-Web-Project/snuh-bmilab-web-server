package com.bmilab.backend.domain.task.entity;

import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_proposals")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TaskProposal extends BaseTimeEntity {

    @Id
    @Column(name = "task_proposal_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "proposal_deadline")
    private LocalDateTime proposalDeadline;

    @Column(name = "contractor_contact_name")
    private String contractorContactName;

    @Column(name = "contractor_contact_department")
    private String contractorContactDepartment;

    @Column(name = "contractor_contact_email")
    private String contractorContactEmail;

    @Column(name = "contractor_contact_phone")
    private String contractorContactPhone;

    @Column(name = "internal_contact_name")
    private String internalContactName;

    @Column(name = "internal_contact_department")
    private String internalContactDepartment;

    @Column(name = "internal_contact_email")
    private String internalContactEmail;

    @Column(name = "internal_contact_phone")
    private String internalContactPhone;

    public void update(
            LocalDateTime proposalDeadline,
            String contractorContactName,
            String contractorContactDepartment,
            String contractorContactEmail,
            String contractorContactPhone,
            String internalContactName,
            String internalContactDepartment,
            String internalContactEmail,
            String internalContactPhone
    ) {
        this.proposalDeadline = proposalDeadline;
        this.contractorContactName = contractorContactName;
        this.contractorContactDepartment = contractorContactDepartment;
        this.contractorContactEmail = contractorContactEmail;
        this.contractorContactPhone = contractorContactPhone;
        this.internalContactName = internalContactName;
        this.internalContactDepartment = internalContactDepartment;
        this.internalContactEmail = internalContactEmail;
        this.internalContactPhone = internalContactPhone;
    }
}
