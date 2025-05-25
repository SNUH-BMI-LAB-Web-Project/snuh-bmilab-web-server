package com.bmilab.backend.domain.report.repository;

import com.bmilab.backend.domain.report.dto.query.GetAllReportsQueryResult;
import java.time.LocalDate;
import java.util.List;

public interface ReportRepositoryCustom {
    List<GetAllReportsQueryResult> findAllWithFilesByFilteringAndUserId(Long userId, Long projectId, LocalDate startDate, LocalDate endDate);
}
