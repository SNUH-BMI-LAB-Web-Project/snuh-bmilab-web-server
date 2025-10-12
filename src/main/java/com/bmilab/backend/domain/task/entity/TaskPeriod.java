package com.bmilab.backend.domain.task.entity;

import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task_periods")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TaskPeriod extends BaseTimeEntity {

    @Id
    @Column(name = "task_period_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Task task;

    @Column(name = "year_number", nullable = false)
    private Integer yearNumber;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User manager;

    @ManyToMany
    @JoinTable(
        name = "task_period_members",
        joinColumns = @JoinColumn(name = "task_period_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private List<User> members = new ArrayList<>();

    public void update(User manager, List<User> members, LocalDate startDate, LocalDate endDate) {
        this.manager = manager;
        this.members.clear();
        this.members.addAll(members);
        this.startDate = startDate;
        this.endDate = endDate;
    }
}