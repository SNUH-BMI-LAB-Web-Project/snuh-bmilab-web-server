package com.bmilab.backend.domain.report.service;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.file.repository.FileInformationRepository;
import com.bmilab.backend.domain.file.service.FileService;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.repository.ProjectRepository;
import com.bmilab.backend.domain.report.dto.query.GetAllReportsQueryResult;
import com.bmilab.backend.domain.report.dto.request.ReportRequest;
import com.bmilab.backend.domain.report.dto.response.ReportFindAllResponse;
import com.bmilab.backend.domain.report.entity.Report;
import com.bmilab.backend.domain.report.exception.ReportErrorCode;
import com.bmilab.backend.domain.report.repository.ReportRepository;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.exception.UserErrorCode;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.exception.ApiException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final FileInformationRepository fileInformationRepository;
    private final FileService fileService;

    @Transactional
    public void createReport(Long userId, ReportRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        Project project = projectRepository.findById(request.projectId())
                .orElse(null);

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        Project project = projectRepository.findById(request.projectId())
                .orElse(null);

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ApiException(ReportErrorCode.REPORT_NOT_FOUND));

        if (!report.isAuthor(user)) {
            throw new ApiException(ReportErrorCode.REPORT_ACCESS_DENIED);
        }

        report.update(project, request.date(), request.content());

        List<FileInformation> files = fileInformationRepository.findAllById(request.fileIds());

        files.forEach(file -> file.updateDomain(FileDomainType.REPORT, report.getId()));
    }

    public ReportFindAllResponse getReportsByCurrentUser(Long userId, Long projectId, LocalDate startDate,
                                                         LocalDate endDate) {
        List<GetAllReportsQueryResult> results = reportRepository.findAllWithFilesByFilteringAndUserId(
                userId, projectId, startDate, endDate);

        return ReportFindAllResponse.of(results);
    }

    @Transactional
    public void deleteReport(Long userId, Long reportId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ApiException(ReportErrorCode.REPORT_NOT_FOUND));

        if (!report.isAuthor(user)) {
            throw new ApiException(ReportErrorCode.REPORT_ACCESS_DENIED);
        }

        fileService.deleteAllFileByDomainTypeAndEntityId(FileDomainType.REPORT, report.getId());
        reportRepository.delete(report);
    }
}
