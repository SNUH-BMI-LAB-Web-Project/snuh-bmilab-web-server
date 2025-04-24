package com.bmilab.backend.domain.project.dto.response;

import com.bmilab.backend.domain.project.dto.response.external.NTISAssignmentResponse.AssignmentItem;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

public record RSSResponse(
        List<RSSItem> items,
        int totalPage
) {
    @Builder
    public record RSSItem(
            String author,
            LocalDate startDate,
            LocalDate endDate,
            String link,
            String title,
            String category,
            LocalDate publishedAt,
            long budget
    ) {
        public static RSSItem from(AssignmentItem item) {
            return RSSItem.builder()
                    .author(item.author())
                    .startDate(item.appbegin())
                    .endDate(item.appdue())
                    .link(item.link())
                    .title(item.title())
                    .category(item.category())
                    .publishedAt(item.pubDate())
                    .budget(item.budget())
                    .build();
        }
    }
}
