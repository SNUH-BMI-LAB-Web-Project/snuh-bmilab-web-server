package com.bmilab.backend.domain.research.paper.entity;

import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.research.paper.enums.ProfessorRole;
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
@Table(name = "research_papers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Paper extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate acceptDate;

    private LocalDate publishDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", nullable = false)
    private Journal journal;

    @Column(nullable = false)
    private String paperTitle;

    @Column(nullable = false, length = 1000)
    private String allAuthors;

    @Column(nullable = false)
    private int authorCount;

    @Column(nullable = false)
    private String firstAuthor;

    private String coAuthors;

    @OneToMany(mappedBy = "paper", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaperAuthor> paperAuthors = new ArrayList<>();

    private String vol;

    private String page;

    @Column(nullable = false)
    private String paperLink;

    @Column(nullable = false)
    private String doi;

    private String pmid;

    private Integer citations;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProfessorRole professorRole;

    private Boolean isRepresentative;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public Paper(LocalDate acceptDate, LocalDate publishDate, Journal journal, String paperTitle, String allAuthors, int authorCount, String firstAuthor, String coAuthors, String vol, String page, String paperLink, String doi, String pmid, Integer citations, ProfessorRole professorRole, Boolean isRepresentative, Task task, Project project) {
        this.acceptDate = acceptDate;
        this.publishDate = publishDate;
        this.journal = journal;
        this.paperTitle = paperTitle;
        this.allAuthors = allAuthors;
        this.authorCount = authorCount;
        this.firstAuthor = firstAuthor;
        this.coAuthors = coAuthors;
        this.vol = vol;
        this.page = page;
        this.paperLink = paperLink;
        this.doi = doi;
        this.pmid = pmid;
        this.citations = citations;
        this.professorRole = professorRole;
        this.isRepresentative = isRepresentative;
        this.task = task;
        this.project = project;
    }

    public void update(LocalDate acceptDate, LocalDate publishDate, Journal journal, String paperTitle, String allAuthors, int authorCount, String firstAuthor, String coAuthors, String vol, String page, String paperLink, String doi, String pmid, Integer citations, ProfessorRole professorRole, Boolean isRepresentative, Task task, Project project) {
        this.acceptDate = acceptDate;
        this.publishDate = publishDate;
        this.journal = journal;
        this.paperTitle = paperTitle;
        this.allAuthors = allAuthors;
        this.authorCount = authorCount;
        this.firstAuthor = firstAuthor;
        this.coAuthors = coAuthors;
        this.vol = vol;
        this.page = page;
        this.paperLink = paperLink;
        this.doi = doi;
        this.pmid = pmid;
        this.citations = citations;
        this.professorRole = professorRole;
        this.isRepresentative = isRepresentative;
        this.task = task;
        this.project = project;
    }

}