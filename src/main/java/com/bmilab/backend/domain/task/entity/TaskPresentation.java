package com.bmilab.backend.domain.task.entity;

import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_presentations")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TaskPresentation extends BaseTimeEntity {

    @Id
    @Column(name = "task_presentation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "presentation_deadline")
    private LocalDateTime presentationDeadline;

    @Column(name = "presentation_date")
    private LocalDateTime presentationDate;

    @Column(name = "presentation_location", columnDefinition = "TEXT")
    private String presentationLocation;

    @Column(name = "presenter")
    private String presenter;

    @Column(name = "attendee_limit")
    private Integer attendeeLimit;

    @Column(name = "attendees", columnDefinition = "TEXT")
    private String attendees;

    public void update(
            LocalDateTime presentationDeadline,
            LocalDateTime presentationDate,
            String presentationLocation,
            String presenter,
            Integer attendeeLimit,
            String attendees
    ) {
        this.presentationDeadline = presentationDeadline;
        this.presentationDate = presentationDate;
        this.presentationLocation = presentationLocation;
        this.presenter = presenter;
        this.attendeeLimit = attendeeLimit;
        this.attendees = attendees;
    }
}
