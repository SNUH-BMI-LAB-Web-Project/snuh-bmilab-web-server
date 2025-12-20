package com.bmilab.backend.domain.research.presentation.dto.response;

import com.bmilab.backend.domain.research.presentation.entity.AcademicPresentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record AcademicPresentationSummaryResponse(
        Long id,
        String academicPresentationName,
        String presentationTitle,
        String authors,
        LocalDate academicPresentationStartDate,
        LocalDate academicPresentationEndDate
) {
    public AcademicPresentationSummaryResponse(AcademicPresentation academicPresentation) {
        this(
                academicPresentation.getId(),
                academicPresentation.getAcademicPresentationName(),
                academicPresentation.getPresentationTitle(),
                academicPresentation.getAuthors(),
                academicPresentation.getAcademicPresentationStartDate(),
                academicPresentation.getAcademicPresentationEndDate()
        );
    }
}
