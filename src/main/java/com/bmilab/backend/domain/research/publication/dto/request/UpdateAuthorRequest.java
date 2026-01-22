package com.bmilab.backend.domain.research.publication.dto.request;

import com.bmilab.backend.domain.research.publication.enums.AuthorType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record UpdateAuthorRequest(
        @Schema(description = "저자 목록", example = "김연구, 박개발")
        String authors,
        @Schema(description = "구분(기고, 저서 중 택1)", example = "CONTRIBUTION")
        AuthorType authorType,
        @Schema(description = "출판일", example = "2026-01-01")
        LocalDate publicationDate,
        @Schema(description = "발행처", example = "BMILAB")
        String publicationHouse,
        @Schema(description = "출판사", example = "인사이트")
        String publisher,
        @Schema(description = "출판물명", example = "의료 빅데이터 분석을 위한 딥러닝")
        String publicationName,
        @Schema(description = "제목", example = "2장. 데이터 전처리")
        String title,
        @Schema(description = "ISBN", example = "978-89-6077-735-4")
        String isbn
) {
}
