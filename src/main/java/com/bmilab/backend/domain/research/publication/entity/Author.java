package com.bmilab.backend.domain.research.publication.entity;

import com.bmilab.backend.domain.research.publication.enums.AuthorType;
import com.bmilab.backend.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Author extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String authors;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthorType authorType;

    @Column(nullable = false)
    private LocalDate publicationDate;

    @Column(nullable = false)
    private String publicationHouse;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private String publicationName;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String isbn;

    @Builder
    public Author(String authors, AuthorType authorType, LocalDate publicationDate, String publicationHouse, String publisher, String publicationName, String title, String isbn) {
        this.authors = authors;
        this.authorType = authorType;
        this.publicationDate = publicationDate;
        this.publicationHouse = publicationHouse;
        this.publisher = publisher;
        this.publicationName = publicationName;
        this.title = title;
        this.isbn = isbn;
    }

    public void update(String authors, AuthorType authorType, LocalDate publicationDate, String publicationHouse, String publisher, String publicationName, String title, String isbn) {
        this.authors = authors;
        this.authorType = authorType;
        this.publicationDate = publicationDate;
        this.publicationHouse = publicationHouse;
        this.publisher = publisher;
        this.publicationName = publicationName;
        this.title = title;
        this.isbn = isbn;
    }
}
