package com.bmilab.backend.domain.research.paper.entity;

import com.bmilab.backend.domain.research.entity.ResearchAuthorEntity;
import com.bmilab.backend.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "research_paper_authors")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaperAuthor extends ResearchAuthorEntity {

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paper_id", nullable = false)
    private Paper paper;

    @Builder
    public PaperAuthor(Paper paper, User user, String role) {
        super(user, role);
        this.paper = paper;
    }
}
