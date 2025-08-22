package com.bmilab.backend.domain.report.repository;

import com.bmilab.backend.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryCustom {
}
