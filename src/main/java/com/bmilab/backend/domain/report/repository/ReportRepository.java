package com.bmilab.backend.domain.report.repository;

import com.bmilab.backend.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
