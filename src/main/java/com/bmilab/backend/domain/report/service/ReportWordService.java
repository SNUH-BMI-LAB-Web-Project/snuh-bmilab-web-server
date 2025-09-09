package com.bmilab.backend.domain.report.service;

import com.bmilab.backend.domain.report.dto.query.GetAllReportsQueryResult;
import com.bmilab.backend.domain.report.repository.ReportRepository;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.utils.ExcelRow;
import com.bmilab.backend.global.utils.WordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReportWordService {

    private final ReportRepository reportRepository;
    private final WordGenerator wordGenerator;
    private final ReportExportConverter reportExportConverter;

    public ByteArrayInputStream getReportWordFileByDateAsBytes(LocalDate date) {

        List<GetAllReportsQueryResult> results = reportRepository.findAllByDateWithFiles(date);
        List<ExcelRow> rows = reportExportConverter.toRows(results);
        try {
            return wordGenerator.generateBy(rows);
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }

    public ByteArrayInputStream getReportWordFileByUserAsBytes(Long userId, LocalDate startDate, LocalDate endDate) {

        List<GetAllReportsQueryResult> results = reportRepository.findReportsByCondition(
                userId,
                null,
                startDate,
                endDate,
                null
        );
        List<ExcelRow> rows = reportExportConverter.toRows(results);
        try {
            return wordGenerator.generateBy(rows);
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }

    public ByteArrayInputStream getReportWordFileByPeriodAsBytes(LocalDate startDate, LocalDate endDate) {

        List<GetAllReportsQueryResult> results = reportRepository.findReportsByCondition(
                null,
                null,
                startDate,
                endDate,
                null
        );
        List<ExcelRow> rows = reportExportConverter.toRows(results);
        try {
            return wordGenerator.generateBy(rows);
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }
}