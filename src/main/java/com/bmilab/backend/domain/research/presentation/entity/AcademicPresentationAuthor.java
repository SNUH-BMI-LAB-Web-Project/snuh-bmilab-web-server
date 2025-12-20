package com.bmilab.backend.domain.research.presentation.entity;

import com.bmilab.backend.domain.research.entity.ResearchAuthorEntity;
import com.bmilab.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AcademicPresentationAuthor extends ResearchAuthorEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_presentation_id", nullable = false)
    private AcademicPresentation academicPresentation;

    @Builder
    public AcademicPresentationAuthor(AcademicPresentation academicPresentation, User user, String role) {
        super(user, role);
        this.academicPresentation = academicPresentation;
    }
}
