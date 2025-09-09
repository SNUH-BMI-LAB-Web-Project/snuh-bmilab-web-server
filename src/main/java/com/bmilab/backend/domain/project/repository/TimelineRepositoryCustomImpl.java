package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.file.entity.QFileInformation;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.project.dto.query.GetAllTimelinesQueryResult;
import com.bmilab.backend.domain.project.entity.QTimeline;
import com.bmilab.backend.domain.project.entity.Timeline;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TimelineRepositoryCustomImpl implements TimelineRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<GetAllTimelinesQueryResult> findAllResultsByProjectId(Long projectId) {
        QTimeline timeline = QTimeline.timeline;
        QFileInformation file = QFileInformation.fileInformation;

        List<Timeline> timelines = queryFactory
                .selectFrom(timeline)
                .where(timeline.project.id.eq(projectId)) // 필요시 조건
                .orderBy(timeline.date.desc())
                .fetch();

        Map<Long, List<FileInformation>> fileMap = queryFactory
                .selectFrom(file)
                .where(
                        file.domainType.eq(FileDomainType.TIMELINE),
                        file.entityId.in(timelines.stream().map(Timeline::getId).toList())
                )
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(FileInformation::getEntityId));

        return timelines.stream()
                .map(t -> new GetAllTimelinesQueryResult(
                        t,
                        fileMap.getOrDefault(t.getId(), List.of())
                ))
                .toList();
    }
}
