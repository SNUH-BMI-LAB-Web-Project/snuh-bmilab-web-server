package com.bmilab.backend.domain.research.presentation.entity;

import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.research.presentation.enums.AcademicPresentationType;
import com.bmilab.backend.domain.task.entity.Task;
import com.bmilab.backend.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "research_academic_presentations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AcademicPresentation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String authors;

    @Column(nullable = false)
    private LocalDate academicPresentationStartDate;

    @Column(nullable = false)
    private LocalDate academicPresentationEndDate;

    @Column(nullable = false)
    private String academicPresentationLocation;

    @Column(nullable = false)
    private String academicPresentationHost;

    @Column(nullable = false)
    private String academicPresentationName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcademicPresentationType presentationType;

    @Column(nullable = false)
    private String presentationTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Builder
    public AcademicPresentation(String authors, LocalDate academicPresentationStartDate, LocalDate academicPresentationEndDate, String academicPresentationLocation, String academicPresentationHost, String academicPresentationName, AcademicPresentationType presentationType, String presentationTitle, Project project, Task task) {
        this.authors = authors;
        this.academicPresentationStartDate = academicPresentationStartDate;
        this.academicPresentationEndDate = academicPresentationEndDate;
        this.academicPresentationLocation = academicPresentationLocation;
        this.academicPresentationHost = academicPresentationHost;
        this.academicPresentationName = academicPresentationName;
        this.presentationType = presentationType;
        this.presentationTitle = presentationTitle;
        this.project = project;
        this.task = task;
    }

    public void update(String authors, LocalDate academicPresentationStartDate, LocalDate academicPresentationEndDate, String academicPresentationLocation, String academicPresentationHost, String academicPresentationName, AcademicPresentationType presentationType, String presentationTitle, Project project, Task task) {
        this.authors = authors;
        this.academicPresentationStartDate = academicPresentationStartDate;
        this.academicPresentationEndDate = academicPresentationEndDate;
        this.academicPresentationLocation = academicPresentationLocation;
        this.academicPresentationHost = academicPresentationHost;
        this.academicPresentationName = academicPresentationName;
        this.presentationType = presentationType;
        this.presentationTitle = presentationTitle;
        this.project = project;
        this.task = task;
    }
}
