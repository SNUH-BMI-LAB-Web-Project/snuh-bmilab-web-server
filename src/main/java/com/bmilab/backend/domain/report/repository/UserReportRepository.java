package com.bmilab.backend.domain.report.repository;

import com.bmilab.backend.domain.report.entity.Report;
import com.bmilab.backend.domain.report.entity.UserReport;
import com.bmilab.backend.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {
    List<UserReport> findAllByReportId(Long reportId);

    List<UserReport> user(User user);

    Optional<UserReport> findByReportAndUser(Report report, User user);
}
