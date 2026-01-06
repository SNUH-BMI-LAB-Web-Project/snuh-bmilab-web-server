package com.bmilab.backend.domain.research.paper.entity;

import com.bmilab.backend.domain.research.paper.enums.JournalCategory;
import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "research_journals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Journal extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String journalName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JournalCategory category;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private String publishCountry;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String issn;

    @Column(nullable = false)
    private String eissn;

    @Column(nullable = false)
    private String jif;  // Journal Impact Factor

    @Column(nullable = false)
    private String jcrRank;

    private String issue;

    @Builder
    public Journal(String journalName, JournalCategory category, String publisher,
                   String publishCountry, String isbn, String issn, String eissn,
                   String jif, String jcrRank, String issue) {
        this.journalName = journalName;
        this.category = category;
        this.publisher = publisher;
        this.publishCountry = publishCountry;
        this.isbn = isbn;
        this.issn = issn;
        this.eissn = eissn;
        this.jif = jif;
        this.jcrRank = jcrRank;
        this.issue = issue;
    }

    public void update(String journalName, JournalCategory category, String publisher,
                      String publishCountry, String isbn, String issn, String eissn,
                      String jif, String jcrRank, String issue) {
        this.journalName = journalName;
        this.category = category;
        this.publisher = publisher;
        this.publishCountry = publishCountry;
        this.isbn = isbn;
        this.issn = issn;
        this.eissn = eissn;
        this.jif = jif;
        this.jcrRank = jcrRank;
        this.issue = issue;
    }
}
