package com.bmilab.backend.domain.research.presentation.entity;

import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AcademicPresentationAuthor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_presentation_id", nullable = false)
    private AcademicPresentation academicPresentation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String role;

    @Builder
    public AcademicPresentationAuthor(AcademicPresentation academicPresentation, User user, String role) {
        this.academicPresentation = academicPresentation;
        this.user = user;
        this.role = role;
    }
}
