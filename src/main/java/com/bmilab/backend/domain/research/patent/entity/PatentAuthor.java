package com.bmilab.backend.domain.research.patent.entity;

import com.bmilab.backend.domain.research.entity.ResearchAuthorEntity;
import com.bmilab.backend.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "research_patent_authors")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatentAuthor extends ResearchAuthorEntity {

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patent_id", nullable = false)
    private Patent patent;

    @Builder
    public PatentAuthor(Patent patent, User user, String role) {
        super(user, role);
        this.patent = patent;
    }
}
