package com.bmilab.backend.domain.board.repository;

import com.bmilab.backend.domain.board.dto.query.GetAllBoardsQueryResult;
import com.bmilab.backend.domain.board.dto.response.BoardCategorySummary;
import com.bmilab.backend.domain.board.entity.QBoard;
import com.bmilab.backend.domain.board.entity.QBoardCategory;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import com.bmilab.backend.domain.user.entity.QUser;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GetAllBoardsQueryResult> findAllByFiltering(
            Long boardId,
            String keyword,
            String category,
            Pageable pageable
    ) {

        QBoard board = QBoard.board;
        QBoardCategory boardCategory = QBoardCategory.boardCategory;
        QUser user = QUser.user;

        BooleanExpression titleContains = keyword != null ? board.title.containsIgnoreCase(keyword) : null;

        BooleanExpression categoryContains = category != null ? board.category.name.containsIgnoreCase(category): null;

        List<GetAllBoardsQueryResult> results = queryFactory.select(Projections.constructor(
                        GetAllBoardsQueryResult.class,
                        board.id,
                        Projections.constructor(
                                UserSummary.class,
                                user.id,
                                user.email,
                                user.name,
                                user.organization,
                                user.department,
                                user.position,
                                user.profileImageUrl
                        ),
                        Projections.constructor(BoardCategorySummary.class, boardCategory.id, boardCategory.name),
                        board.title,
                        board.viewCount
                ))
                .from(board)
                .leftJoin(board.author, user)
                .leftJoin(board.category, boardCategory)
                .where(titleContains, categoryContains)
                .orderBy(getBoardSortOrderSpecifier(pageable, board))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = Optional.ofNullable(queryFactory.select(board.count())
                .from(board)
                .where(titleContains, categoryContains)
                .fetchOne()).orElse(0L);

        return PageableExecutionUtils.getPage(results, pageable, () -> count);
    }

    private OrderSpecifier<?>[] getBoardSortOrderSpecifier(Pageable pageable, QBoard board) {
        Sort sort = pageable.getSort();

        if (sort.isUnsorted()) {
            return new OrderSpecifier[]{board.createdAt.desc()};
        }

        for (Sort.Order order : sort) {
            boolean isDesc = order.isDescending();
            String property = order.getProperty();

            if ("createdAt".equals(property)) {
                return new OrderSpecifier[]{
                        isDesc ? board.createdAt.desc() : board.createdAt.asc()
                };
            }
        }

        return new OrderSpecifier[]{board.createdAt.desc()};
    }
}
