package com.bmilab.backend.domain.research.paper.service;

import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.service.FileService;
import com.bmilab.backend.domain.project.entity.ExternalProfessor;
import com.bmilab.backend.domain.project.repository.ExternalProfessorRepository;
import com.bmilab.backend.domain.research.paper.exception.PaperErrorCode;
import com.bmilab.backend.domain.research.paper.dto.request.CreatePaperRequest;
import com.bmilab.backend.domain.research.paper.dto.request.UpdatePaperRequest;
import com.bmilab.backend.domain.research.paper.dto.response.PaperFindAllResponse;
import com.bmilab.backend.domain.research.paper.dto.response.PaperResponse;
import com.bmilab.backend.domain.research.paper.dto.response.PaperSummaryResponse;
import com.bmilab.backend.domain.research.paper.entity.Journal;
import com.bmilab.backend.domain.research.paper.entity.Paper;
import com.bmilab.backend.domain.research.paper.entity.PaperAuthor;
import com.bmilab.backend.domain.research.paper.entity.PaperCorrespondingAuthor;
import com.bmilab.backend.domain.research.paper.repository.JournalRepository;
import com.bmilab.backend.domain.research.paper.repository.PaperAuthorRepository;
import com.bmilab.backend.domain.research.paper.repository.PaperCorrespondingAuthorRepository;
import com.bmilab.backend.domain.research.paper.repository.PaperRepository;
import com.bmilab.backend.domain.research.service.AuthorSyncService;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.exception.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PaperService {

    private final PaperRepository paperRepository;
    private final PaperAuthorRepository paperAuthorRepository;
    private final PaperCorrespondingAuthorRepository paperCorrespondingAuthorRepository;
    private final JournalRepository journalRepository;
    private final ExternalProfessorRepository externalProfessorRepository;
    private final FileService fileService;
    private final AuthorSyncService authorSyncService;

    public PaperResponse createPaper(CreatePaperRequest dto) {
        Journal journal = journalRepository.findById(dto.journalId())
                .orElseThrow(() -> new ApiException(PaperErrorCode.JOURNAL_NOT_FOUND));
        int authorCount = (dto.allAuthors() != null) ? dto.allAuthors().split(",").length : 0;
        Paper newPaper = Paper.builder()
                .acceptDate(dto.acceptDate())
                .publishDate(dto.publishDate())
                .journal(journal)
                .paperTitle(dto.paperTitle())
                .allAuthors(dto.allAuthors())
                .authorCount(authorCount)
                .firstAuthor(dto.firstAuthor())
                .coAuthors(dto.coAuthors())
                .vol(dto.vol())
                .page(dto.page())
                .paperLink(dto.paperLink())
                .doi(dto.doi())
                .pmid(dto.pmid())
                .citations(dto.citations())
                .professorRole(dto.professorRole())
                .isRepresentative(dto.isRepresentative())
                .build();
        paperRepository.save(newPaper);

        fileService.syncFiles(dto.fileIds(), FileDomainType.PAPER_ATTACHMENT, newPaper.getId());
        List<FileSummary> fileSummaries = fileService.findAllByDomainTypeAndEntityId(FileDomainType.PAPER_ATTACHMENT, newPaper.getId())
                .stream()
                .map(FileSummary::from)
                .toList();

        List<PaperAuthor> paperAuthors = authorSyncService.syncAuthors(
                dto.paperAuthors(),
                CreatePaperRequest.PaperAuthorRequest::userId,
                CreatePaperRequest.PaperAuthorRequest::role,
                (user, role) -> PaperAuthor.builder()
                        .paper(newPaper)
                        .user(user)
                        .role(role)
                        .build()
        );
        paperAuthors.forEach(paperAuthorRepository::save);

        // Handle PaperCorrespondingAuthor linking
        if (dto.correspondingAuthors() != null && !dto.correspondingAuthors().isEmpty()) {
            List<Long> externalProfessorIds = dto.correspondingAuthors().stream()
                    .map(CreatePaperRequest.PaperCorrespondingAuthorRequest::externalProfessorId)
                    .collect(Collectors.toList());
            List<ExternalProfessor> externalProfessors = externalProfessorRepository.findAllById(externalProfessorIds);
            if (externalProfessors.size() != externalProfessorIds.size()) {
                throw new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND);
            }
            java.util.Map<Long, ExternalProfessor> professorMap = externalProfessors.stream()
                    .collect(Collectors.toMap(ExternalProfessor::getId, professor -> professor));

            for (CreatePaperRequest.PaperCorrespondingAuthorRequest correspondingAuthorRequest : dto.correspondingAuthors()) {
                ExternalProfessor professor = professorMap.get(correspondingAuthorRequest.externalProfessorId());
                PaperCorrespondingAuthor correspondingAuthor = PaperCorrespondingAuthor.builder()
                        .paper(newPaper)
                        .externalProfessor(professor)
                        .role(correspondingAuthorRequest.role())
                        .build();
                paperCorrespondingAuthorRepository.save(correspondingAuthor);
            }
        }

        List<PaperCorrespondingAuthor> correspondingAuthorList = paperCorrespondingAuthorRepository.findAllByPaperId(newPaper.getId());
        return new PaperResponse(newPaper, correspondingAuthorList, paperAuthors, fileSummaries);
    }

    public void deletePaper(Long userId, boolean isAdmin, Long paperId) {
        if (!isAdmin) {
            throw new ApiException(PaperErrorCode.PAPER_ACCESS_DENIED);
        }
        paperAuthorRepository.deleteAllByPaperId(paperId); // Explicitly delete PaperAuthors
        fileService.deleteAllFileByDomainTypeAndEntityId(FileDomainType.PAPER_ATTACHMENT, paperId);
        paperRepository.deleteById(paperId);
    }

    @Transactional(readOnly = true)
    public PaperResponse getPaper(Long paperId) {
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new ApiException(PaperErrorCode.PAPER_NOT_FOUND));
        List<FileSummary> fileSummaries = fileService.findAllByDomainTypeAndEntityId(FileDomainType.PAPER_ATTACHMENT, paperId)
                .stream()
                .map(FileSummary::from)
                .toList();
        List<PaperCorrespondingAuthor> correspondingAuthors = paperCorrespondingAuthorRepository.findAllByPaperId(paperId);
        List<PaperAuthor> paperAuthors = paperAuthorRepository.findAllByPaperId(paperId);
        return new PaperResponse(paper, correspondingAuthors, paperAuthors, fileSummaries);
    }

    public PaperResponse updatePaper(Long paperId, UpdatePaperRequest dto) {
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new ApiException(PaperErrorCode.PAPER_NOT_FOUND));
        Journal journal = journalRepository.findById(dto.journalId())
                .orElseThrow(() -> new ApiException(PaperErrorCode.JOURNAL_NOT_FOUND));
        int authorCount = (dto.allAuthors() != null) ? dto.allAuthors().split(",").length : 0;
        paper.update(dto.acceptDate(), dto.publishDate(), journal, dto.paperTitle(), dto.allAuthors(), authorCount, dto.firstAuthor(), dto.coAuthors(), dto.vol(), dto.page(), dto.paperLink(), dto.doi(), dto.pmid(), dto.citations(), dto.professorRole(), dto.isRepresentative());

        // Handle PaperCorrespondingAuthor linking
        paperCorrespondingAuthorRepository.deleteAllByPaperId(paperId);
        if (dto.correspondingAuthors() != null && !dto.correspondingAuthors().isEmpty()) {
            List<Long> externalProfessorIds = dto.correspondingAuthors().stream()
                    .map(UpdatePaperRequest.PaperCorrespondingAuthorRequest::externalProfessorId)
                    .collect(Collectors.toList());
            List<ExternalProfessor> externalProfessors = externalProfessorRepository.findAllById(externalProfessorIds);
            if (externalProfessors.size() != externalProfessorIds.size()) {
                throw new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND);
            }
            java.util.Map<Long, ExternalProfessor> professorMap = externalProfessors.stream()
                    .collect(Collectors.toMap(ExternalProfessor::getId, professor -> professor));

            for (UpdatePaperRequest.PaperCorrespondingAuthorRequest correspondingAuthorRequest : dto.correspondingAuthors()) {
                ExternalProfessor professor = professorMap.get(correspondingAuthorRequest.externalProfessorId());
                PaperCorrespondingAuthor correspondingAuthor = PaperCorrespondingAuthor.builder()
                        .paper(paper)
                        .externalProfessor(professor)
                        .role(correspondingAuthorRequest.role())
                        .build();
                paperCorrespondingAuthorRepository.save(correspondingAuthor);
            }
        }

        fileService.syncFiles(dto.fileIds(), FileDomainType.PAPER_ATTACHMENT, paperId);
        List<FileSummary> fileSummaries = fileService.findAllByDomainTypeAndEntityId(FileDomainType.PAPER_ATTACHMENT, paperId)
                .stream()
                .map(FileSummary::from)
                .toList();

        // Update PaperAuthors
        paperAuthorRepository.deleteAllByPaperId(paperId); // Delete existing
        List<PaperAuthor> paperAuthors = authorSyncService.syncAuthors(
                dto.paperAuthors(),
                UpdatePaperRequest.PaperAuthorRequest::userId,
                UpdatePaperRequest.PaperAuthorRequest::role,
                (user, role) -> PaperAuthor.builder()
                        .paper(paper)
                        .user(user)
                        .role(role)
                        .build()
        );
        paperAuthors.forEach(paperAuthorRepository::save);

        List<PaperCorrespondingAuthor> correspondingAuthorList = paperCorrespondingAuthorRepository.findAllByPaperId(paperId);
        return new PaperResponse(paper, correspondingAuthorList, paperAuthors, fileSummaries);
    }

    @Transactional(readOnly = true)
    public PaperFindAllResponse getPapers(String keyword, Pageable pageable) {
        Page<Paper> paperPage = paperRepository.findAllBy(keyword, pageable);

        List<PaperSummaryResponse> papers = paperPage.getContent().stream()
                .map(paper -> {
                    List<PaperCorrespondingAuthor> correspondingAuthors =
                            paperCorrespondingAuthorRepository.findAllByPaperId(paper.getId());
                    List<PaperAuthor> paperAuthors =
                            paperAuthorRepository.findAllByPaperId(paper.getId());
                    List<FileSummary> files = fileService.findAllByDomainTypeAndEntityId(
                                    FileDomainType.PAPER_ATTACHMENT, paper.getId())
                            .stream()
                            .map(FileSummary::from)
                            .toList();
                    return PaperSummaryResponse.from(paper, correspondingAuthors, paperAuthors, files);
                })
                .toList();

        return PaperFindAllResponse.of(papers, paperPage.getTotalPages());
    }
}
