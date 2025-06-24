package com.bmilab.backend.domain.project.dto.response;

import com.bmilab.backend.domain.project.dto.query.SearchProjectQueryResult;
import java.util.List;
import lombok.Builder;

public record SearchProjectResponse(
        List<SearchProjectItem> projects
) {
    public static SearchProjectResponse of(List<SearchProjectQueryResult> results) {
        return new SearchProjectResponse(
                results
                        .stream()
                        .map(SearchProjectItem::from)
                        .toList()
        );
    }

    @Builder
    public record SearchProjectItem(
            Long projectId,
            String title
    ) {
        public static SearchProjectItem from(SearchProjectQueryResult queryResult) {
            return SearchProjectItem.builder()
                    .projectId(queryResult.projectId())
                    .title(queryResult.title())
                    .build();
        }
    }
}
