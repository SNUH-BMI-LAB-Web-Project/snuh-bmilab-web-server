package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.dto.query.GetAllTimelinesQueryResult;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TimelineRepositoryCustomImpl implements TimelineRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<GetAllTimelinesQueryResult> findAllResultsByProjectId(Long projectId) {
        return Collections.emptyList();
    }
}
