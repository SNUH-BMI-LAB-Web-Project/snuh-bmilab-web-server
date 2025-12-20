package com.bmilab.backend.domain.research.publication.dto.response;

import com.bmilab.backend.domain.research.publication.entity.Author;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record AuthorResponse(
        @Schema(description = "저서 ID", example = "1")
        Long id,
        @Schema(description = "저자 목록", example = "김연구, 이개발")
        String authors,
        @Schema(description = "구분", example = "Contribution")
        String authorType,
        @Schema(description = "출판일", example = "2025-12-05")
        LocalDate publicationDate,
        @Schema(description = "발행처", example = "BMILAB")
        String publicationHouse,
        @Schema(description = "출판사", example = "에이콘")
        String publisher,
        @Schema(description = "출판물명", example = "패혈증 조기 발견을 위한 임상 의사결정 지원 시스템")
        String publicationName,
        @Schema(description = "제목", example = "1장. 시스템 개요")
        String title,
        @Schema(description = "ISBN", example = "978-89-6077-735-3")
        String isbn
) {
    public AuthorResponse(Author author) {
        this(
                author.getId(),
                author.getAuthors(),
                author.getAuthorType().getDescription(),
                author.getPublicationDate(),
                author.getPublicationHouse(),
                author.getPublisher(),
                author.getPublicationName(),
                author.getTitle(),
                author.getIsbn()
        );
    }
}