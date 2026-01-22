package com.bmilab.backend.domain.research.publication.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record AuthorFindAllResponse(
        @Schema(description = "저서 목록")
        List<AuthorSummaryResponse> authors,

        @Schema(description = "전체 페이지 수", example = "5")
        int totalPage
) {
    public static AuthorFindAllResponse from(Page<AuthorSummaryResponse> page) {
        return AuthorFindAllResponse.builder()
                .authors(page.getContent())
                .totalPage(page.getTotalPages())
                .build();
    }
}
