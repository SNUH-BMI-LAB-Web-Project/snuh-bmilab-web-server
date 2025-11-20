package com.bmilab.backend.domain.task.entity;

import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "publications")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Publication extends BaseTimeEntity {

    @Id
    @Column(name = "publication_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Task task;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String authors;

    @Column(nullable = false)
    private String journal;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(columnDefinition = "TEXT")
    private String doi;

    public void update(String title, String authors, String journal, LocalDate publicationDate, String doi) {
        this.title = title;
        this.authors = authors;
        this.journal = journal;
        this.publicationDate = publicationDate;
        this.doi = doi;
    }
}
