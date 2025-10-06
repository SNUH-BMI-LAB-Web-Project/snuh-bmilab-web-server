package com.bmilab.backend.domain.task.entity;

import com.bmilab.backend.domain.task.enums.TaskProfessorRole;
import com.bmilab.backend.domain.task.enums.TaskStatus;
import com.bmilab.backend.domain.task.enums.TaskSupportType;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Task extends BaseTimeEntity {

    @Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "research_task_number", unique = true, nullable = false)
    private String researchTaskNumber;

    @Column(nullable = false)
    private String title;

    @Column(name = "rfp_number", nullable = false)
    private String rfpNumber;

    @Column(name = "rfp_name", columnDefinition = "TEXT", nullable = false)
    private String rfpName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Column(name = "business_name", columnDefinition = "TEXT", nullable = false)
    private String businessName;

    @Column(name = "issuing_agency", nullable = false)
    private String issuingAgency;

    @Enumerated(EnumType.STRING)
    @Column(name = "support_type", nullable = false)
    private TaskSupportType supportType;

    @Column(name = "three_five_rule", columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean threeFiveRule;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_years", nullable = false)
    private Integer totalYears;

    @Column(name = "current_year", nullable = false)
    private Integer currentYear;

    @Column(name = "lead_institution", nullable = false)
    private String leadInstitution;

    @Column(name = "lead_professor", nullable = false)
    private String leadProfessor;

    @Column(name = "snuh_pi", columnDefinition = "TEXT", nullable = false)
    private String snuhPi;

    @Enumerated(EnumType.STRING)
    @Column(name = "professor_role", nullable = false)
    private TaskProfessorRole professorRole;

    @ManyToOne
    @JoinColumn(name = "practical_manager_id", nullable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User practicalManager;

    @Column(name = "participating_institutions", columnDefinition = "TEXT", nullable = false)
    private String participatingInstitutions;

    public void update(
            String researchTaskNumber,
            String title,
            String rfpNumber,
            String rfpName,
            TaskStatus status,
            String businessName,
            String issuingAgency,
            TaskSupportType supportType,
            Boolean threeFiveRule,
            LocalDate startDate,
            LocalDate endDate,
            Integer totalYears,
            Integer currentYear,
            String leadInstitution,
            String leadProfessor,
            String snuhPi,
            TaskProfessorRole professorRole,
            User practicalManager,
            String participatingInstitutions
    ) {
        this.researchTaskNumber = researchTaskNumber;
        this.title = title;
        this.rfpNumber = rfpNumber;
        this.rfpName = rfpName;
        this.status = status;
        this.businessName = businessName;
        this.issuingAgency = issuingAgency;
        this.supportType = supportType;
        this.threeFiveRule = threeFiveRule;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalYears = totalYears;
        this.currentYear = currentYear;
        this.leadInstitution = leadInstitution;
        this.leadProfessor = leadProfessor;
        this.snuhPi = snuhPi;
        this.professorRole = professorRole;
        this.practicalManager = practicalManager;
        this.participatingInstitutions = participatingInstitutions;
    }

    public boolean canBeEditedByUser(Long userId) {
        if (this.status != TaskStatus.PROPOSAL_REJECTED
                && this.status != TaskStatus.PRESENTATION_REJECTED
                && this.status != TaskStatus.COMPLETED) {
            return true;
        }

        return this.practicalManager != null && this.practicalManager.getId().equals(userId);
    }
}