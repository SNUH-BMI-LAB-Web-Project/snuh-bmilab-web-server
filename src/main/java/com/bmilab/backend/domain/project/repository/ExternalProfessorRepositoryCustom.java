package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.dto.ExternalProfessorSummary;
import java.util.List;

public interface ExternalProfessorRepositoryCustom {
    List<ExternalProfessorSummary> findExists(Iterable<ExternalProfessorSummary> keys);
}
