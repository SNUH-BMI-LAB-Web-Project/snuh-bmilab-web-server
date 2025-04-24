package com.bmilab.backend.domain.project.dto.response.external;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.List;

public record NTISAssignmentResponse(
        List<AssignmentItem> items
) {
    @JsonIgnore
    public int getTotalCount() {
        if (items.isEmpty()) {
            return 0;
        }

        return items.get(0).totCnt;
    }

    public record AssignmentItem(
            int rNum,
            int totCnt,
            String author,
            LocalDate appbegin,
            LocalDate appdue,
            String link,
            String title,
            String category,
            @JsonFormat(pattern = "yyyy.MM.dd")
            LocalDate pubDate,
            long budget
    ) {
    }
}
