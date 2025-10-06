package com.bmilab.backend.domain.task.repository;

import com.bmilab.backend.domain.task.entity.QTask;
import com.bmilab.backend.domain.task.entity.Task;
import com.bmilab.backend.domain.task.enums.TaskStatus;
import com.bmilab.backend.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class TaskRepositoryCustomImpl implements TaskRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Task> findTasksForList(TaskStatus status, String keyword, Pageable pageable) {
        QTask task = QTask.task;
        QUser user = QUser.user;

        List<Task> content = queryFactory
                .selectFrom(task)
                .leftJoin(task.practicalManager, user).fetchJoin()
                .where(
                        status != null ? task.status.eq(status) : null,
                        keyword != null && !keyword.isBlank() ? task.title.containsIgnoreCase(keyword) : null
                )
                .orderBy(task.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(task.count())
                .from(task)
                .where(
                        status != null ? task.status.eq(status) : null,
                        keyword != null && !keyword.isBlank() ? task.title.containsIgnoreCase(keyword) : null
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
