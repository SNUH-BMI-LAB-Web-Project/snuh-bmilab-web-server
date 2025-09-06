package com.bmilab.backend.domain.report.controller;

import com.bmilab.backend.domain.report.dto.response.ReportFindAllResponse;
import com.bmilab.backend.domain.report.service.ReportExcelService;
import com.bmilab.backend.domain.report.service.ReportService;
import com.bmilab.backend.domain.report.service.ReportWordService;
import com.bmilab.backend.global.utils.ExcelGenerator;
import com.bmilab.backend.global.utils.WordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

@RestController
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class AdminReportController implements AdminReportApi{

    private final ReportService reportService;
    private final ReportExcelService reportExcelService;
    private final ReportWordService reportWordService;

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

    @GetMapping("/excel")
    public ResponseEntity<InputStreamResource> createReportExcel(
            @RequestParam LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {

        ByteArrayInputStream excel = reportExcelService.getReportExcelFileByPeriodAsBytes(startDate, endDate);
        MediaType excelMediaType = MediaType.valueOf(ExcelGenerator.EXCEL_MEDIA_TYPE);

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=snuh-bmilab-daily-report(" + startDate + "~" + endDate + ").xlsx")
                .contentType(excelMediaType)
                .body(new InputStreamResource(excel));
    }

    @GetMapping("/word")
    public ResponseEntity<InputStreamResource> getWordFileByCurrentUser(
            @RequestParam LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ){
        ByteArrayInputStream word = reportWordService.getReportWordFileByPeriodAsBytes(startDate, endDate);
        MediaType wordMediaType = MediaType.valueOf(WordGenerator.WORD_MEDIA_TYPE);

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=snuh-bmilab-daily-report(" + startDate + "~" + endDate + ").docx")
                .contentType(wordMediaType)
                .body(new InputStreamResource(word));
    }
}
