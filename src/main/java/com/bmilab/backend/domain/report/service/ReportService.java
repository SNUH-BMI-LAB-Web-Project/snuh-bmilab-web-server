package com.bmilab.backend.domain.report.service;

import com.bmilab.backend.domain.report.dto.request.ReportRequest;
import com.bmilab.backend.domain.report.dto.response.ReportDetail;
import com.bmilab.backend.domain.report.dto.response.ReportSummary;
import com.bmilab.backend.domain.report.dto.response.ReportFindAllResponse;
import com.bmilab.backend.domain.report.dto.response.UserReportFindAllResponse;
import com.bmilab.backend.domain.report.dto.response.UserReportSummary;
import com.bmilab.backend.domain.report.entity.Report;
import com.bmilab.backend.domain.report.entity.UserReport;
import com.bmilab.backend.domain.report.exception.ReportErrorCode;
import com.bmilab.backend.domain.report.repository.ReportRepository;
import com.bmilab.backend.domain.report.repository.UserReportRepository;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.exception.UserErrorCode;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.external.s3.S3Service;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserReportRepository userReportRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Transactional
    public void createReport(ReportRequest request) {
        Report report = Report
                .builder()
                .tag(request.tag())
                .dueDate(request.dueDate())
                .build();

        reportRepository.save(report);
    }

    public UserReportFindAllResponse getAllReportsByUser(Long userId) {
        List<UserReport> userReports = userReportRepository.findAllByUserId(userId);

        return UserReportFindAllResponse.of(userReports);
    }

    public ReportFindAllResponse getAllReports(int pageNo, String criteria) {
        PageRequest pageRequest = PageRequest.of(pageNo, 10, Sort.by(Direction.DESC, criteria));
        List<ReportSummary> reportSummaries = reportRepository.findAll(pageRequest)
                .stream()
                .map(ReportSummary::from)
                .toList();

        return ReportFindAllResponse.of(reportSummaries);
    }

    @Transactional
    public void updateReport(Long reportId, ReportRequest request) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ApiException(ReportErrorCode.REPORT_NOT_FOUND));

        report.update(request.tag(), request.dueDate());
    }

    @Transactional
    public void deleteReport(List<Long> reportIds) {
        try {
            reportIds.forEach(reportRepository::deleteById);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ReportErrorCode.REPORT_NOT_FOUND);
        }
    }

    @Transactional
    public void submitReport(Long reportId, Long userId, MultipartFile file) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ApiException(ReportErrorCode.REPORT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        Optional<UserReport> userReportOptional = userReportRepository.findByReportAndUser(report, user);

        String fileUrl = uploadReportFile(file, report, user);

        if (userReportOptional.isPresent()) {
            UserReport userReport = userReportOptional.get();
            userReport.updateFileUrl(fileUrl);
            return;
        }

        UserReport newUserReport = UserReport
                .builder()
                .report(report)
                .user(user)
                .fileUrl(fileUrl)
                .build();

        userReportRepository.save(newUserReport);
    }

    private String uploadReportFile(MultipartFile file, Report report, User user) {
        String newFileDir = "reports/" + report.getId() + "/" + report.getTag() + "_" + user.getName();
        return s3Service.uploadFile(file, newFileDir);
    }

    public ReportDetail getReportById(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ApiException(ReportErrorCode.REPORT_NOT_FOUND));

        List<UserReportSummary> userReports = userReportRepository.findAllByReportId(reportId)
                .stream()
                .map(UserReportSummary::from)
                .toList();

        return ReportDetail.from(report, userReports);
    }
}
