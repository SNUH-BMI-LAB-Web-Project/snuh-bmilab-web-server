package com.bmilab.backend.domain.report.service;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.repository.FileInformationRepository;
import com.bmilab.backend.domain.file.service.FileService;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.service.ProjectService;
import com.bmilab.backend.domain.report.dto.query.GetAllReportsQueryResult;
import com.bmilab.backend.domain.report.dto.request.ReportRequest;
import com.bmilab.backend.domain.report.dto.response.ReportFindAllResponse;
import com.bmilab.backend.domain.report.entity.Report;
import com.bmilab.backend.domain.report.exception.ReportErrorCode;
import com.bmilab.backend.domain.report.repository.ReportRepository;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.service.UserService;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.utils.ExcelGenerator;
import com.bmilab.backend.global.utils.ExcelRow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserService userService;
    private final FileInformationRepository fileInformationRepository;
    private final FileService fileService;
    private final ProjectService projectService;
    private final ExcelGenerator excelGenerator;

    public Report findReportById(Long reportId) {

        return reportRepository.findById(reportId)
                .orElseThrow(() -> new ApiException(ReportErrorCode.REPORT_NOT_FOUND));
    }

    @Transactional
    public void createReport(Long userId, ReportRequest request) {

        User user = userService.findUserById(userId);

        Long projectId = request.projectId();

        Project project = (projectId != null) ? projectService.findProjectById(projectId) : null;

        Report report = Report.builder()
                .user(user)
                .date(request.date())
                .project(project)
                .content(request.content())
                .build();

        reportRepository.save(report);

        List<FileInformation> files = fileInformationRepository.findAllById(request.fileIds());

        files.forEach(file -> file.updateDomain(FileDomainType.REPORT, report.getId()));
    }


    @Transactional
    public void updateReport(Long userId, Long reportId, ReportRequest request) {

        User user = userService.findUserById(userId);

        Long projectId = request.projectId();

        Project project = (projectId != null) ? projectService.findProjectById(projectId) : null;

        Report report = findReportById(reportId);

        validateUserIsReportAuthor(user, report);

        report.update(project, request.date(), request.content());

        List<FileInformation> files = fileInformationRepository.findAllById(request.fileIds());

        files.forEach(file -> file.updateDomain(FileDomainType.REPORT, report.getId()));
    }

    public ReportFindAllResponse getReportsByCurrentUser(
            Long userId,
            Long projectId,
            LocalDate startDate,
            LocalDate endDate
    ) {

        List<GetAllReportsQueryResult> results = reportRepository.findReportsByUser(
                userId,
                projectId,
                startDate,
                endDate
        );

        return ReportFindAllResponse.of(results);
    }

    public ReportFindAllResponse getReportsByAllUser(
            Long userId,
            Long projectId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword
    ) {

        List<GetAllReportsQueryResult> results = reportRepository.findReportsByCondition(
                userId,
                projectId,
                startDate,
                endDate,
                keyword
        );

        return ReportFindAllResponse.of(results);
    }

    @Transactional
    public void deleteReport(Long userId, Long reportId) {

        User user = userService.findUserById(userId);

        Report report = findReportById(reportId);

        validateUserIsReportAuthor(user, report);

        fileService.deleteAllFileByDomainTypeAndEntityId(FileDomainType.REPORT, report.getId());
        reportRepository.delete(report);
    }

    private void validateUserIsReportAuthor(User user, Report report) {

        if (!report.isAuthor(user)) {
            throw new ApiException(ReportErrorCode.REPORT_ACCESS_DENIED);
        }
    }
}
