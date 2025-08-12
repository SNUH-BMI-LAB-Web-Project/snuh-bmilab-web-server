package com.bmilab.backend.domain.report.service;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.report.dto.query.GetAllReportsQueryResult;
import com.bmilab.backend.domain.report.entity.Report;
import com.bmilab.backend.domain.report.repository.ReportRepository;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.utils.ExcelGenerator;
import com.bmilab.backend.global.utils.ExcelRow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportExcelService {

    private final ReportRepository reportRepository;
    private final ExcelGenerator excelGenerator;
    private static final String[] HEADER_TITLES = { "보고 일자", "이름", "이메일", "연구명", "보고 내용", "첨부파일 URL" };

    public List<ExcelRow> generateExcelRows(List<GetAllReportsQueryResult> results) {

        return results.stream().map((result) -> {
            Report report = result.report();
            List<FileInformation> files = result.files();
            String date = report.getDate().toString();
            String userName = report.getUser().getName();
            String email = report.getUser().getEmail();
            String projectTitle = report.getProject().getTitle();
            String content = report.getContent();
            List<String> fileUrls = files.stream().map(FileInformation::getUploadUrl).toList();

            return ExcelRow.of(date, userName, email, projectTitle, content, String.join("\n", fileUrls));
        }).toList();
    }

    public ByteArrayInputStream getReportExcelFileByDateAsBytes(LocalDate date) {

        List<GetAllReportsQueryResult> results = reportRepository.findAllByDateWithFiles(date);
        List<ExcelRow> excelRows = generateExcelRows(results);

        try {
            return excelGenerator.generateBy(HEADER_TITLES, excelRows);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException(e);
        }
    }

    public File getReportExcelFileByDateAsFile(LocalDate date) {

        List<GetAllReportsQueryResult> results = reportRepository.findAllByDateWithFiles(date);
        List<ExcelRow> excelRows = generateExcelRows(results);

        try {
            return excelGenerator.generateExcelFile(HEADER_TITLES, excelRows, "bmilab-report_" + date + ".xlsx");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(e);
        }
    }

    public ByteArrayInputStream getReportExcelFileByUserAsBytes(Long userId) {

        List<GetAllReportsQueryResult> results = reportRepository.findAllByUser(userId);
        List<ExcelRow> excelRows = generateExcelRows(results);

        try {
            return excelGenerator.generateBy(HEADER_TITLES, excelRows);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException(e);
        }
    }

    public ByteArrayInputStream getReportExcelFileByPeriodAsBytes(LocalDate startDate, LocalDate endDate) {

        List<GetAllReportsQueryResult> results = reportRepository.findReportsByCondition(
                null,
                null,
                startDate,
                endDate,
                null
        );

        List<ExcelRow> excelRows = generateExcelRows(results);

        try {
            return excelGenerator.generateBy(HEADER_TITLES, excelRows);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException(e);
        }
    }
}
