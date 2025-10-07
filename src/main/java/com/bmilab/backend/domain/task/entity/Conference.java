package com.bmilab.backend.domain.task.entity;

import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "conferences")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Conference extends BaseTimeEntity {

    @Id
    @Column(name = "conference_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Task task;

    @Column(name = "presentation_title", nullable = false)
    private String presentationTitle;

    @Column(name = "conference_name", nullable = false)
    private String conferenceName;

    @Column(nullable = false)
    private String presenter;

    @Column(name = "presentation_date")
    private LocalDate presentationDate;

    public void update(String presentationTitle, String conferenceName, String presenter, LocalDate presentationDate) {
        this.presentationTitle = presentationTitle;
        this.conferenceName = conferenceName;
        this.presenter = presenter;
        this.presentationDate = presentationDate;
    }
}
