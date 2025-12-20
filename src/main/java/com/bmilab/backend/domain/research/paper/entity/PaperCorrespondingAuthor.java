package com.bmilab.backend.domain.research.paper.entity;

import com.bmilab.backend.domain.project.entity.ExternalProfessor;
import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaperCorrespondingAuthor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paper_id", nullable = false)
    private Paper paper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ex_professor_id", nullable = false)
    private ExternalProfessor externalProfessor;

    private String role;

    @Builder
    public PaperCorrespondingAuthor(Paper paper, ExternalProfessor externalProfessor, String role) {
        this.paper = paper;
        this.externalProfessor = externalProfessor;
        this.role = role;
    }
}
