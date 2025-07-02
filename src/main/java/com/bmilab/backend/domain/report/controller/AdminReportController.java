package com.bmilab.backend.domain.report.controller;

import com.bmilab.backend.domain.report.dto.response.ReportFindAllResponse;
import com.bmilab.backend.domain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class AdminReportController implements AdminReportApi{

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<ReportFindAllResponse> getReportsByAllUser(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String keyword
    ) {

        return ResponseEntity.ok(reportService.getReportsByAllUser(
                userId,
                projectId,
                startDate,
                endDate,
                keyword
        ));
    }
}
