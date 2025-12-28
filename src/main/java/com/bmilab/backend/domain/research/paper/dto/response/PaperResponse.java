package com.bmilab.backend.domain.research.paper.dto.response;

import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.research.paper.entity.Paper;
import com.bmilab.backend.domain.research.paper.entity.PaperAuthor;
import com.bmilab.backend.domain.research.paper.entity.PaperCorrespondingAuthor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record PaperResponse(
        @Schema(description = "논문 ID")
        Long id,
        @Schema(description = "Accept 날짜")
        LocalDate acceptDate,
        @Schema(description = "Publish 날짜")
        LocalDate publishDate,
        @Schema(description = "저널 정보")
        JournalResponse journal,
        @Schema(description = "논문 제목")
        String paperTitle,
        @Schema(description = "전체 저자")
        String allAuthors,
        @Schema(description = "저자 수")
        int authorCount,
        @Schema(description = "제1저자")
        String firstAuthor,
        @Schema(description = "공동저자")
        String coAuthors,
        @Schema(description = "교신저자 목록 (외부 교수)")
        List<PaperCorrespondingAuthorResponse> correspondingAuthors,
        @Schema(description = "연구실 내 저자 목록")
        List<PaperAuthorResponse> paperAuthors,
        @Schema(description = "Vol")
        String vol,
        @Schema(description = "Page")
        String page,
        @Schema(description = "논문 링크")
        String paperLink,
        @Schema(description = "DOI")
        String doi,
        @Schema(description = "PMID")
        String pmid,
        @Schema(description = "인용 횟수")
        int citations,
        @Schema(description = "김광수 교수님 역할")
        String professorRole,
        @Schema(description = "대표 실적 여부")
        boolean isRepresentative,
        @Schema(description = "첨부 파일 목록")
        List<FileSummary> files
) {
    public PaperResponse(Paper paper, List<PaperCorrespondingAuthor> correspondingAuthors, List<PaperAuthor> paperAuthors, List<FileSummary> files) {
        this(
                paper.getId(),
                paper.getAcceptDate(),
                paper.getPublishDate(),
                JournalResponse.from(paper.getJournal()),
                paper.getPaperTitle(),
                paper.getAllAuthors(),
                paper.getAuthorCount(),
                paper.getFirstAuthor(),
                paper.getCoAuthors(),
                correspondingAuthors.stream().map(PaperCorrespondingAuthorResponse::new).collect(Collectors.toList()),
                paperAuthors.stream().map(PaperAuthorResponse::new).collect(Collectors.toList()),
                paper.getVol(),
                paper.getPage(),
                paper.getPaperLink(),
                paper.getDoi(),
                paper.getPmid(),
                paper.getCitations(),
                paper.getProfessorRole().getDescription(),
                paper.getIsRepresentative(),
                files
        );
    }

    public record PaperCorrespondingAuthorResponse(
            @Schema(description = "외부 교수 ID")
            Long externalProfessorId,
            @Schema(description = "외부 교수명")
            String externalProfessorName,
            @Schema(description = "역할")
            String role
    ) {
        public PaperCorrespondingAuthorResponse(PaperCorrespondingAuthor correspondingAuthor) {
            this(
                    correspondingAuthor.getExternalProfessor().getId(),
                    correspondingAuthor.getExternalProfessor().getName(),
                    correspondingAuthor.getRole()
            );
        }
    }

    public record PaperAuthorResponse(
            @Schema(description = "사용자 ID")
            Long userId,
            @Schema(description = "사용자명")
            String userName,
            @Schema(description = "역할")
            String role
    ) {
        public PaperAuthorResponse(PaperAuthor paperAuthor) {
            this(
                    paperAuthor.getUser().getId(),
                    paperAuthor.getUser().getName(),
                    paperAuthor.getRole()
            );
        }
    }
}
