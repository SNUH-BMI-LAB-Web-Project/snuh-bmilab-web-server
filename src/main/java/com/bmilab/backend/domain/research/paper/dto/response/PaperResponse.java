package com.bmilab.backend.domain.research.paper.dto.response;

import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.research.paper.entity.Paper;
import com.bmilab.backend.domain.research.paper.entity.PaperAuthor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record PaperResponse(
        Long id,
        LocalDate acceptDate,
        LocalDate publishDate,
        JournalResponse journal,
        String paperTitle,
        String allAuthors,
        int authorCount,
        String firstAuthor,
        String coAuthors,
        List<PaperAuthorResponse> paperAuthors,
        String vol,
        String page,
        String paperLink,
        String doi,
        String pmid,
        int citations,
        String professorRole,
        boolean isRepresentative,
        List<FileSummary> files
) {
    public PaperResponse(Paper paper, List<PaperAuthor> paperAuthors, List<FileSummary> files) {
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

    public record PaperAuthorResponse(
            Long userId,
            String userName,
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
