package com.bmilab.backend.domain.research.paper.entity;

import com.bmilab.backend.domain.research.paper.enums.JournalCategory;
import com.bmilab.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "research_journals", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"journal_name", "year"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Journal extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "journal_name", nullable = false)
    private String journalName;

    @Column(nullable = false)
    private Integer year;

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
    public Journal(String journalName, Integer year, JournalCategory category, String publisher,
                   String publishCountry, String isbn, String issn, String eissn,
                   String jif, String jcrRank, String issue) {
        this.journalName = journalName;
        this.year = year;
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

    public void update(String journalName, Integer year, JournalCategory category, String publisher,
                      String publishCountry, String isbn, String issn, String eissn,
                      String jif, String jcrRank, String issue) {
        this.journalName = journalName;
        this.year = year;
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
