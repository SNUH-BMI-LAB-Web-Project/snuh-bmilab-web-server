package com.bmilab.backend.domain.report.controller;

import com.bmilab.backend.domain.report.dto.request.ReportRequest;
import com.bmilab.backend.domain.report.dto.response.ReportFindAllResponse;
import com.bmilab.backend.domain.report.service.ReportExcelService;
import com.bmilab.backend.domain.report.service.ReportService;
import com.bmilab.backend.global.security.UserAuthInfo;
import com.bmilab.backend.global.utils.ExcelGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
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
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController implements ReportApi {

    private final ReportService reportService;
    private final ReportExcelService reportExcelService;

    @PostMapping
    public ResponseEntity<Void> createReport(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid ReportRequest request
    ) {

        reportService.createReport(userAuthInfo.getUserId(), request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<Void> updateReport(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long reportId,
            @RequestBody @Valid ReportRequest request
    ) {

        reportService.updateReport(userAuthInfo.getUserId(), reportId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteReport(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long reportId
    ) {

        reportService.deleteReport(userAuthInfo.getUserId(), reportId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ReportFindAllResponse> getReportsByCurrentUser(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {

        return ResponseEntity.ok(reportService.getReportsByCurrentUser(
                userAuthInfo.getUserId(),
                projectId,
                startDate,
                endDate
        ));
    }

    @GetMapping("/excel")
    public ResponseEntity<InputStreamResource> getExcelFileByCurrentUser(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {

        Long userId = userAuthInfo.getUserId();
        ByteArrayInputStream excel = reportExcelService.getReportExcelFileByUserAsBytes(userId, startDate, endDate);
        MediaType excelMediaType = MediaType.valueOf(ExcelGenerator.EXCEL_MEDIA_TYPE);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=report_" + userId + ".xlsx")
                .contentType(excelMediaType)
                .body(new InputStreamResource(excel));
    }
}
