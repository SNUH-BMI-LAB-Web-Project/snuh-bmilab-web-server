package com.bmilab.backend.domain.report.controller;

import com.bmilab.backend.domain.report.dto.request.DeleteReportRequest;
import com.bmilab.backend.domain.report.dto.request.ReportRequest;
import com.bmilab.backend.domain.report.dto.response.ReportDetail;
import com.bmilab.backend.domain.report.dto.response.ReportFindAllResponse;
import com.bmilab.backend.domain.report.dto.response.UserReportFindAllResponse;
import com.bmilab.backend.domain.report.service.ReportService;
import com.bmilab.backend.global.security.UserAuthInfo;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController implements ReportApi {
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<Void> createReport(@RequestBody ReportRequest request) {
        reportService.createReport(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ReportFindAllResponse> getAllReports(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            @RequestParam(required = false, defaultValue = "createdAt", value = "criteria") String criteria
    ) {
        return ResponseEntity.ok(reportService.getAllReports(pageNo, criteria));
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportDetail> getReportById(@PathVariable Long reportId) {
        return ResponseEntity.ok(reportService.getReportById(reportId));
    }

    @GetMapping("/me")
    public ResponseEntity<UserReportFindAllResponse> getAllReportsByUser(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo
    ) {
        return ResponseEntity.ok(reportService.getAllReportsByUser(userAuthInfo.getUserId()));
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<Void> updateReport(@PathVariable Long reportId, @RequestBody ReportRequest request) {
        reportService.updateReport(reportId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteReport(@RequestBody DeleteReportRequest request) {
        reportService.deleteReport(request.reportIds());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{reportId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> submitReport(@PathVariable Long reportId,
                                             @AuthenticationPrincipal UserAuthInfo principal,
                                             @RequestPart MultipartFile file) {
        reportService.submitReport(reportId, principal.getUserId(), file);
        return ResponseEntity.ok().build();
    }
}
