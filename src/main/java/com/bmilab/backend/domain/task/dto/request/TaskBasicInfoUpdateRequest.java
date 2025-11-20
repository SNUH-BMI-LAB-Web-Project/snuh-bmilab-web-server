package com.bmilab.backend.domain.task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TaskBasicInfoUpdateRequest(

        @Schema(description = "소관부처", example = "산업통상자원부")
        String ministry,

        @Schema(description = "전문기관", example = "한국산업기술평가관리원")
        String specializedAgency,

        @Schema(description = "공고번호", example = "RFP-2025-001")
        String announcementNumber,

        @Schema(description = "3책5공", example = "true")
        Boolean threeFiveRule,

        @Schema(description = "공고 시작일", example = "2025-02-01")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate announcementStartDate,

        @Schema(description = "공고 종료일", example = "2027-02-28")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate announcementEndDate,

        @Schema(description = "사업담당자 이름", example = "홍길동")
        String businessContactName,

        @Schema(description = "사업담당자 소속", example = "산업기술혁신과")
        String businessContactDepartment,

        @Schema(description = "사업담당자 이메일", example = "hong@example.com")
        String businessContactEmail,

        @Schema(description = "사업담당자 전화번호", example = "010-1234-5678")
        String businessContactPhone,

        @Schema(description = "공고 링크", example = "https://example.or.kr/announcement/2025-001")
        String announcementLink,

        @Schema(description = "RFP 파일 ID 목록")
        List<UUID> rfpFileIds,

        @Schema(description = "공고서류 파일 ID 목록")
        List<UUID> announcementFileIds
) {
}
