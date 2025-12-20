package com.bmilab.backend.domain.research.patent.entity;

import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.task.entity.Task;
import com.bmilab.backend.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Patent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate applicationDate;

    @Column(nullable = false)
    private String applicationNumber;

    @Column(nullable = false)
    private String patentName;

    @Column(nullable = false)
    private String applicantsAll;

    @OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatentAuthor> patentAuthors = new ArrayList<>();

    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Builder
    public Patent(LocalDate applicationDate, String applicationNumber, String patentName, String applicantsAll, String remarks, Project project, Task task) {
        this.applicationDate = applicationDate;
        this.applicationNumber = applicationNumber;
        this.patentName = patentName;
        this.applicantsAll = applicantsAll;
        this.remarks = remarks;
        this.project = project;
        this.task = task;
    }

    public void update(LocalDate applicationDate, String applicationNumber, String patentName, String applicantsAll, String remarks, Project project, Task task) {
        this.applicationDate = applicationDate;
        this.applicationNumber = applicationNumber;
        this.patentName = patentName;
        this.applicantsAll = applicantsAll;
        this.remarks = remarks;
        this.project = project;
        this.task = task;
    }

}
