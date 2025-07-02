package com.bmilab.backend.domain.report.repository;

import com.bmilab.backend.domain.report.dto.query.GetAllReportsQueryResult;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepositoryCustom {

    List<GetAllReportsQueryResult> findReportsByUser(
            Long userId,
            Long projectId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<GetAllReportsQueryResult> findReportsByCondition(
            Long userId,
            Long projectId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword
    );
}
