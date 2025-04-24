package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.entity.QProjectParticipant;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProjectParticipantRepositoryCustomImpl implements ProjectParticipantRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<Long> findAllUserIdsByProjectIdAndLeader(Long projectId, boolean leader) {
        QProjectParticipant projectParticipant = QProjectParticipant.projectParticipant;

        return queryFactory
                .select(projectParticipant.user.id)
                .from(projectParticipant)
                .where(
                        projectParticipant.project.id.eq(projectId)
                                .and(projectParticipant.isLeader.eq(leader))
                )
                .fetch();
    }
}
