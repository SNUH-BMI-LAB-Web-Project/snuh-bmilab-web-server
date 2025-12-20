package com.bmilab.backend.domain.research.service;

import com.bmilab.backend.domain.research.paper.entity.*;
import com.bmilab.backend.domain.research.paper.repository.*;
import com.bmilab.backend.domain.research.paper.enums.*;
import com.bmilab.backend.domain.research.patent.entity.*;
import com.bmilab.backend.domain.research.patent.repository.*;
import com.bmilab.backend.domain.research.award.entity.*;
import com.bmilab.backend.domain.research.award.repository.*;
import com.bmilab.backend.domain.research.presentation.entity.*;
import com.bmilab.backend.domain.research.presentation.repository.*;
import com.bmilab.backend.domain.research.presentation.enums.*;
import com.bmilab.backend.domain.research.publication.entity.*;
import com.bmilab.backend.domain.research.publication.repository.*;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.service.FileService;
import com.bmilab.backend.domain.research.award.entity.Award;
import com.bmilab.backend.domain.research.presentation.entity.AcademicPresentation;
import com.bmilab.backend.domain.research.paper.entity.Paper;
import com.bmilab.backend.domain.research.patent.entity.Patent;
import com.bmilab.backend.domain.research.publication.entity.Author;
import com.bmilab.backend.domain.research.award.repository.AwardRepository;
import com.bmilab.backend.domain.research.presentation.repository.AcademicPresentationRepository;
import com.bmilab.backend.domain.research.paper.repository.PaperRepository;
import com.bmilab.backend.domain.research.patent.repository.PatentRepository;
import com.bmilab.backend.domain.research.publication.repository.AuthorRepository;
import com.bmilab.backend.global.utils.ExcelGenerator;
import com.bmilab.backend.global.utils.ExcelRow;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResearchExcelService {

    private final ExcelGenerator excelGenerator;
    @Qualifier("researchAuthorRepository")
    private final AuthorRepository authorRepository;
    private final AcademicPresentationRepository academicPresentationRepository;
    private final AwardRepository awardRepository;
    private final PaperRepository paperRepository;
    private final PatentRepository patentRepository;
    private final FileService fileService; // Needed for Paper/Patent files, but won't be used directly in Excel generation

    // Author
    public byte[] exportPublicationsToExcel() {
        String[] HEADER_TITLES = {
                "ID", "저자", "구분", "출판일", "발행처", "출판사", "출판물명", "제목", "ISBN"
        };
        List<Author> authors = authorRepository.findAll(Sort.by(Sort.Direction.DESC, "publicationDate"));
        List<ExcelRow> excelRows = authors.stream()
                .map(author -> ExcelRow.of(
                        String.valueOf(author.getId()),
                        author.getAuthors(),
                        author.getAuthorType().getDescription(),
                        author.getPublicationDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        author.getPublicationHouse(),
                        author.getPublisher(),
                        author.getPublicationName(),
                        author.getTitle(),
                        author.getIsbn()
                ))
                .collect(Collectors.<ExcelRow>toList());

        try (ByteArrayInputStream is = excelGenerator.generateBy(HEADER_TITLES, excelRows)) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file for publications", e);
        }
    }

    // AcademicPresentation
    public byte[] exportAcademicPresentationsToExcel() {
        String[] HEADER_TITLES = {
                "ID", "발표자", "학회 시작일", "학회 종료일", "학회 장소", "학회 주최", "학회명", "발표 타입", "발표 제목", "연계 프로젝트 ID", "연계 프로젝트명"
        };
        List<AcademicPresentation> academicPresentations = academicPresentationRepository.findAll(Sort.by(Sort.Direction.DESC, "academicPresentationStartDate"));
        List<ExcelRow> excelRows = academicPresentations.stream()
                .map(academicPresentation -> ExcelRow.of(
                        String.valueOf(academicPresentation.getId()),
                        academicPresentation.getAuthors(),
                        academicPresentation.getAcademicPresentationStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        academicPresentation.getAcademicPresentationEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        academicPresentation.getAcademicPresentationLocation(),
                        academicPresentation.getAcademicPresentationHost(),
                        academicPresentation.getAcademicPresentationName(),
                        academicPresentation.getPresentationType().getDescription(),
                        academicPresentation.getPresentationTitle(),
                        String.valueOf(academicPresentation.getProject().getId()),
                        academicPresentation.getProject().getTitle()
                ))
                .collect(Collectors.<ExcelRow>toList());

        try (ByteArrayInputStream is = excelGenerator.generateBy(HEADER_TITLES, excelRows)) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file for academic presentations", e);
        }
    }

    // Award
    public byte[] exportAwardsToExcel() {
        String[] HEADER_TITLES = {
                "ID", "수상자", "수상 날짜", "주최 기관", "대회명", "수상명", "발표 제목", "연계 프로젝트 ID", "연계 프로젝트명"
        };
        List<Award> awards = awardRepository.findAll(Sort.by(Sort.Direction.DESC, "awardDate"));
        List<ExcelRow> excelRows = awards.stream()
                .map(award -> ExcelRow.of(
                        String.valueOf(award.getId()),
                        award.getRecipients(),
                        award.getAwardDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        award.getHostInstitution(),
                        award.getCompetitionName(),
                        award.getAwardName(),
                        award.getPresentationTitle(),
                        String.valueOf(award.getProject().getId()),
                        award.getProject().getTitle()
                ))
                .collect(Collectors.<ExcelRow>toList());

        try (ByteArrayInputStream is = excelGenerator.generateBy(HEADER_TITLES, excelRows)) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file for awards", e);
        }
    }

    // Paper
    public byte[] exportPapersToExcel() {
        String[] HEADER_TITLES = {
                "ID", "Accept 날짜", "Publish 날짜", "저널명", "논문 제목", "전체 저자", "제1저자", "공동저자", "저자 수", "연구실 저자", "Vol", "Page", "논문 링크", "DOI", "PMID", "인용 횟수", "교수님 역할", "대표 실적 여부", "첨부 파일"
        };
        List<Paper> papers = paperRepository.findAll(Sort.by(Sort.Direction.DESC, "acceptDate"));
        List<ExcelRow> excelRows = papers.stream()
                .map(paper -> {
                    String paperAuthors = paper.getPaperAuthors().stream()
                            .map(pa -> pa.getUser().getName() + "(" + pa.getRole() + ")")
                            .collect(Collectors.joining(", "));

                    String fileNames = fileService.findAllByDomainTypeAndEntityId(FileDomainType.PAPER_ATTACHMENT, paper.getId())
                            .stream()
                            .map(file -> file.getName() + "(" + file.getUploadUrl() + ")")
                            .collect(Collectors.joining(", "));

                    return ExcelRow.of(
                            String.valueOf(paper.getId()),
                            paper.getAcceptDate() != null ? paper.getAcceptDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : "",
                            paper.getPublishDate() != null ? paper.getPublishDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : "",
                            paper.getJournal().getJournalName(),
                            paper.getPaperTitle(),
                            paper.getAllAuthors(),
                            paper.getFirstAuthor(),
                            paper.getCoAuthors() != null ? paper.getCoAuthors() : "",
                            String.valueOf(paper.getAuthorCount()),
                            paperAuthors,
                            paper.getVol() != null ? paper.getVol() : "",
                            paper.getPage() != null ? paper.getPage() : "",
                            paper.getPaperLink(),
                            paper.getDoi(),
                            paper.getPmid() != null ? paper.getPmid() : "",
                            String.valueOf(paper.getCitations()),
                            paper.getProfessorRole().getDescription(),
                            paper.getIsRepresentative() != null && paper.getIsRepresentative() ? "Y" : "N",
                            fileNames
                    );
                })
                .collect(Collectors.<ExcelRow>toList());

        try (ByteArrayInputStream is = excelGenerator.generateBy(HEADER_TITLES, excelRows)) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file for papers", e);
        }
    }

    // Patent
    public byte[] exportPatentsToExcel() {
        String[] HEADER_TITLES = {
                "ID", "출원일자", "출원번호", "출원명", "출원인(전체)", "출원인(연구실)", "비고", "연계 프로젝트 ID", "연계 프로젝트명", "첨부 파일"
        };
        List<Patent> patents = patentRepository.findAll(Sort.by(Sort.Direction.DESC, "applicationDate"));
        List<ExcelRow> excelRows = patents.stream()
                .map(patent -> {
                    String patentAuthors = patent.getPatentAuthors().stream()
                            .map(pa -> pa.getUser().getName() + "(" + pa.getRole() + ")")
                            .collect(Collectors.joining(", "));

                    String fileNames = fileService.findAllByDomainTypeAndEntityId(FileDomainType.PATENT_ATTACHMENT, patent.getId())
                            .stream()
                            .map(file -> file.getName() + "(" + file.getUploadUrl() + ")")
                            .collect(Collectors.joining(", "));

                    return ExcelRow.of(
                            String.valueOf(patent.getId()),
                            patent.getApplicationDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                            patent.getApplicationNumber(),
                            patent.getPatentName(),
                            patent.getApplicantsAll(),
                            patentAuthors,
                            patent.getRemarks() != null ? patent.getRemarks() : "",
                            String.valueOf(patent.getProject().getId()),
                            patent.getProject().getTitle(),
                            fileNames
                    );
                })
                .collect(Collectors.<ExcelRow>toList());

        try (ByteArrayInputStream is = excelGenerator.generateBy(HEADER_TITLES, excelRows)) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file for patents", e);
        }
    }
}