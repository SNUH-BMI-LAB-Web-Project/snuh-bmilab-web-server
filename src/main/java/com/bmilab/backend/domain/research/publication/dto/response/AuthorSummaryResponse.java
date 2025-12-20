package com.bmilab.backend.domain.research.publication.dto.response;

import com.bmilab.backend.domain.research.publication.entity.Author;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record AuthorSummaryResponse(
        Long id,
        String publicationName,
        String title,
        String authors,
        LocalDate publicationDate
) {
    public AuthorSummaryResponse(Author author) {
        this(
                author.getId(),
                author.getPublicationName(),
                author.getTitle(),
                author.getAuthors(),
                author.getPublicationDate()
        );
    }
}