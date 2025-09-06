package com.bmilab.backend.domain.report.service;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.report.dto.query.GetAllReportsQueryResult;
import com.bmilab.backend.domain.report.entity.Report;
import com.bmilab.backend.global.utils.ExcelRow;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReportExportConverter {

    public List<ExcelRow> toRows(List<GetAllReportsQueryResult> results) {
        return results.stream().map(result -> {
            Report report = result.report();
            List<FileInformation> files = result.files();

            String fileUrls = files.stream()
                    .map(FileInformation::getUploadUrl)
                    .collect(Collectors.joining("\n"));

            return ExcelRow.of(
                    report.getDate().toString(),
                    report.getUser().getName(),
                    report.getUser().getEmail(),
                    report.getProject().getTitle(),
                    report.getContent(),
                    fileUrls
            );
        }).toList();
    }
}