package com.bmilab.backend.domain.research.paper.dto.request;

import com.bmilab.backend.domain.research.paper.enums.ProfessorRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreatePaperRequest(
        @Schema(description = "Accept 날짜", example = "2025-10-20")
        LocalDate acceptDate,
        @Schema(description = "Publish 날짜", example = "2025-12-05")
        LocalDate publishDate,
        @Schema(description = "저널 ID", example = "1")
        Long journalId,
        @Schema(description = "논문 제목", example = "The impact of AI on medical diagnostics")
        String paperTitle,
        @Schema(description = "전체 저자 (쉼표로 구분)", example = "김연구, 이개발, 박석사")
        String allAuthors,
        @Schema(description = "제1저자", example = "김연구")
        String firstAuthor,
        @Schema(description = "공동저자 (외 몇 명)", example = "외 2명")
        String coAuthors,
        @Schema(description = "교신 저자 (외부 교수)")
        List<PaperCorrespondingAuthorRequest> correspondingAuthors,
        @Schema(description = "연구실 내 저자 정보")
        List<PaperAuthorRequest> paperAuthors,
        @Schema(description = "Vol", example = "10")
        String vol,
        @Schema(description = "Page", example = "123-128")
        String page,
        @Schema(description = "논문 링크", example = "https://www.nature.com/articles/...")
        String paperLink,
        @Schema(description = "DOI", example = "10.1038/s41586-023-06692-z")
        String doi,
        @Schema(description = "PMID", example = "37919634")
        String pmid,
        @Schema(description = "인용 횟수", example = "15")
        int citations,
        @Schema(description = "김광수 교수님 역할", example = "CORRESPONDING_AUTHOR")
        ProfessorRole professorRole,
        @Schema(description = "대표 실적 여부", example = "true")
        boolean isRepresentative,
        @Schema(description = "첨부 파일 ID 목록", example = "[\"a1b2c3d4-e5f6-7890-1234-567890abcdef\"]")
        List<UUID> fileIds
) {
    @Schema(description = "논문 저자 요청 DTO")
    public record PaperAuthorRequest(
            @Schema(description = "사용자 ID", example = "1")
            Long userId,
            @Schema(description = "저자 역할", example = "제1저자")
            String role
    ) {}

    @Schema(description = "논문 교신저자 요청 DTO")
    public record PaperCorrespondingAuthorRequest(
            @Schema(description = "외부 교수 ID", example = "1")
            Long externalProfessorId,
            @Schema(description = "역할 (선택적)", example = "교신저자")
            String role
    ) {}
}
