package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.dto.query.GetAllTimelinesQueryResult;
import java.util.List;

public interface TimelineRepositoryCustom {
    List<GetAllTimelinesQueryResult> findAllResultsByProjectId(Long projectId);
}
