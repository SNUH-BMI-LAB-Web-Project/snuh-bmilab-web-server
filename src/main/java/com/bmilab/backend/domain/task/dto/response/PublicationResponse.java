package com.bmilab.backend.domain.task.dto.response;

import com.bmilab.backend.domain.task.entity.Publication;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record PublicationResponse(
        @Schema(description = "논문 ID")
        Long id,

        @Schema(description = "논문 제목")
        String title,

        @Schema(description = "저자")
        String authors,

        @Schema(description = "학술지명")
        String journal,

        @Schema(description = "게재일")
        LocalDate publicationDate,

        @Schema(description = "DOI")
        String doi
) {
    public static PublicationResponse from(Publication publication) {
        return new PublicationResponse(
                publication.getId(),
                publication.getTitle(),
                publication.getAuthors(),
                publication.getJournal(),
                publication.getPublicationDate(),
                publication.getDoi()
        );
    }
}
