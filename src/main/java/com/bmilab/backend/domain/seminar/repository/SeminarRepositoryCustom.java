package com.bmilab.backend.domain.seminar.repository;

import com.bmilab.backend.domain.seminar.entity.Seminar;
import com.bmilab.backend.domain.seminar.enums.SeminarLabel;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SeminarRepositoryCustom {
    List<Seminar> findAllByDateRange(LocalDate startDate, LocalDate endDate);

    Page<Seminar> searchSeminars(String keyword, SeminarLabel label, Pageable pageable);
}
