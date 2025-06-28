package com.bmilab.backend.domain.project.entity;

import com.bmilab.backend.domain.project.enums.ProjectStatus;
import com.bmilab.backend.domain.projectcategory.entity.ProjectCategory;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.enums.Role;
import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "projects")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Project extends BaseTimeEntity {
    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private ProjectCategory category;

    @Column(nullable = false)
    private ProjectStatus status;

    @Column
    private String pi;

    @Column(name = "practical_professor")
    private String practicalProfessor;

    @Column(name = "irb_id")
    private String irbId;

    @Column(name = "drb_id")
    private String drbId;

    @Builder.Default
    @Column(name = "is_private", columnDefinition = "TINYINT(1)")
    private boolean isPrivate = false;

    public boolean canBeEditedBy(User user) {
        return author.getId().equals(user.getId()) || user.getRole() == Role.ADMIN;
    }

    public void update(
            String title,
            String content,
            LocalDate startDate,
            LocalDate endDate,
            List<String> piList,
            List<String> practicalProfessorList,
            ProjectCategory category,
            ProjectStatus status,
            boolean isPrivate
    ) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pi = String.join(",", piList);
        this.practicalProfessor = String.join(",", practicalProfessorList);
        this.category = category;
        this.status = status;
        this.isPrivate = isPrivate;
    }

    public void complete(LocalDate endDate) {
        this.endDate = endDate;
        this.status = ProjectStatus.COMPLETED;
    }

    public List<String> getPIList() {
        return List.of(pi.split(","));
    }

    public List<String> getPracticalProfessorList() {
        return List.of(practicalProfessor.split(","));
    }
}
