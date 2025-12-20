package com.bmilab.backend.domain.research.award.entity;

import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.task.entity.Task;
import com.bmilab.backend.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Award extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String recipients;

    @Column(nullable = false)
    private LocalDate awardDate;

    @Column(nullable = false)
    private String hostInstitution;

    @Column(nullable = false)
    private String competitionName;

    @Column(nullable = false)
    private String awardName;

    @Column(nullable = false)
    private String presentationTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Builder
    public Award(String recipients, LocalDate awardDate, String hostInstitution, String competitionName, String awardName, String presentationTitle, Project project, Task task) {
        this.recipients = recipients;
        this.awardDate = awardDate;
        this.hostInstitution = hostInstitution;
        this.competitionName = competitionName;
        this.awardName = awardName;
        this.presentationTitle = presentationTitle;
        this.project = project;
        this.task = task;
    }

    public void update(String recipients, LocalDate awardDate, String hostInstitution, String competitionName, String awardName, String presentationTitle, Project project, Task task) {
        this.recipients = recipients;
        this.awardDate = awardDate;
        this.hostInstitution = hostInstitution;
        this.competitionName = competitionName;
        this.awardName = awardName;
        this.presentationTitle = presentationTitle;
        this.project = project;
        this.task = task;
    }
}
