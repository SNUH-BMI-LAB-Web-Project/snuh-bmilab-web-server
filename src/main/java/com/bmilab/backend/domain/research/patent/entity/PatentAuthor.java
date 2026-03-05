package com.bmilab.backend.domain.research.patent.entity;

import com.bmilab.backend.domain.project.entity.ExternalProfessor;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.global.entity.BaseTimeEntity;
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
public class PatentAuthor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patent_id", nullable = false)
    private Patent patent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ex_professor_id")
    private ExternalProfessor externalProfessor;

    @Column(nullable = false)
    private String role;

    @Builder
    public PatentAuthor(Patent patent, User user, ExternalProfessor externalProfessor, String role) {
        this.patent = patent;
        this.user = user;
        this.externalProfessor = externalProfessor;
        this.role = role;
    }

    public boolean isInternal() {
        return user != null;
    }

    public boolean isExternal() {
        return externalProfessor != null;
    }

    public String getAuthorName() {
        if (user != null) {
            return user.getName();
        }
        if (externalProfessor != null) {
            return externalProfessor.getName();
        }
        return null;
    }
}
