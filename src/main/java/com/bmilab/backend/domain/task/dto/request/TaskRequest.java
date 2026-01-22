package com.bmilab.backend.domain.task.dto.request;

import com.bmilab.backend.domain.task.enums.TaskProfessorRole;
import com.bmilab.backend.domain.task.enums.TaskStatus;
import com.bmilab.backend.domain.task.enums.TaskSupportType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TaskRequest(
        @Schema(description = "연구과제번호", example = "RS-2025-0000001")
        @NotBlank(message = "연구과제번호는 필수입니다.")
        String researchTaskNumber,

        @Schema(description = "과제명", example = "AI 기반 스마트 팩토리 시스템 개발")
        @NotBlank(message = "과제명은 필수입니다.")
        String title,

        @Schema(description = "RFP번호", example = "RFP-2024-001")
        @NotBlank(message = "RFP번호는 필수입니다.")
        String rfpNumber,

        @Schema(description = "RFP명", example = "2025년 스마트제조혁신기술개발사업 공고")
        @NotBlank(message = "RFP명은 필수입니다.")
        String rfpName,

        @Schema(description = "사업명", example = "스마트제조혁신기술개발사업")
        @NotBlank(message = "사업명은 필수입니다.")
        String businessName,

        @Schema(description = "발주처", example = "산업통상자원부")
        @NotBlank(message = "발주처는 필수입니다.")
        String issuingAgency,

        @Schema(description = "연구과제지원", example = "TOTAL(총괄)")
        @NotNull(message = "연구과제지원은 필수입니다.")
        TaskSupportType supportType,

        @Schema(description = "3책5공 포함 여부", example = "true")
        @NotNull(message = "3책5공 포함 여부는 필수입니다.")
        Boolean threeFiveRule,

        @Schema(description = "총 연차", example = "3")
        @NotNull(message = "총 연차는 필수입니다.")
        Integer totalYears,

        @Schema(description = "현재 연차", example = "1")
        @NotNull(message = "현재 연차는 필수입니다.")
        Integer currentYear,

        @Schema(description = "과제 연차별 기간 목록")
        List<TaskPeriodRequest> periods,

        @Schema(description = "주관기관", example = "한국과학기술원")
        @NotBlank(message = "주관기관은 필수입니다.")
        String leadInstitution,

        @Schema(description = "담당교수", example = "김철수 교수")
        @NotBlank(message = "담당교수는 필수입니다.")
        String leadProfessor,

        @Schema(description = "SNUH PI", example = "이상훈 교수")
        @NotBlank(message = "SNUH PI는 필수입니다.")
        String snuhPi,

        @Schema(description = "김광수교수님 역할", example = "공동연구자")
        @NotNull(message = "김광수교수님 역할은 필수입니다.")
        TaskProfessorRole professorRole,

        @Schema(description = "실무 책임자 ID", example = "123")
        @NotNull(message = "실무 책임자는 필수입니다.")
        Long practicalManagerId,

        @Schema(description = "참여기관", example = "한국과학기술원 (주관기관), 삼성전자 (공동연구기관), LG전자 (위탁연구기관)")
        @NotBlank(message = "참여기관은 필수입니다.")
        String participatingInstitutions,

        @Schema(description = "진행 상태", example = "PROPOSAL_WRITING(제안서 준비중)")
        TaskStatus status,

        @Schema(description = "원내과제 여부", example = "true")
        @NotNull(message = "원내과제 여부는 필수입니다.")
        Boolean isInternal
) {
}