package com.bmilab.backend.domain.research.patent.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record UpdatePatentRequest(
        @Schema(description = "출원일자", example = "2025-05-11")
        LocalDate applicationDate,
        @Schema(description = "출원번호", example = "10-2025-0058212")
        String applicationNumber,
        @Schema(description = "출원명", example = "인공지능 기반 의료 빅데이터 분석 장치 및 방법")
        String patentName,
        @Schema(description = "출원인(전체)", example = "서울대학교병원, 김연구, 이개발")
        String applicantsAll,
        @Schema(description = "출원인(연구실)")
        List<PatentAuthorRequest> patentAuthors,
        @Schema(description = "비고", example = "국내/미국 특허")
        String remarks,
        @Schema(description = "연계 프로젝트 ID", example = "2")
        Long projectId,
        @Schema(description = "연계 과제 ID (선택적)", example = "1")
        Long taskId,
        @Schema(description = "첨부 파일 ID 목록", example = "[\"a1b2c3d4-e5f6-7890-1234-567890abcdef\"]")
        List<java.util.UUID> fileIds
) {
    @Schema(description = "특허 저자 요청 DTO")
    public record PatentAuthorRequest(
            @Schema(description = "사용자 ID", example = "1")
            Long userId,
            @Schema(description = "저자 역할", example = "발명자")
            String role
    ) {}
}
