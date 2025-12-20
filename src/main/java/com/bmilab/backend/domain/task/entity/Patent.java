package com.bmilab.backend.domain.task.entity;

import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity(name = "TaskPatent")
@Table(name = "patents")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Patent extends BaseTimeEntity {

    @Id
    @Column(name = "patent_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Task task;

    @Column(name = "patent_title", nullable = false)
    private String patentTitle;

    @Column(name = "patent_number")
    private String patentNumber;

    @Column(name = "application_date")
    private LocalDate applicationDate;

    public void update(String patentTitle, String patentNumber, LocalDate applicationDate) {
        this.patentTitle = patentTitle;
        this.patentNumber = patentNumber;
        this.applicationDate = applicationDate;
    }
}
