package com.bmilab.backend.domain.task.dto.response;

import com.bmilab.backend.domain.task.entity.Task;
import com.bmilab.backend.domain.task.enums.TaskProfessorRole;
import com.bmilab.backend.domain.task.enums.TaskStatus;
import com.bmilab.backend.domain.task.enums.TaskSupportType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record TaskSummaryResponse(
        @Schema(description = "과제 ID")
        Long id,

        @Schema(description = "연구과제번호")
        String researchTaskNumber,

        @Schema(description = "과제명")
        String title,

        @Schema(description = "RFP번호")
        String rfpNumber,

        @Schema(description = "RFP명")
        String rfpName,

        @Schema(description = "진행 상태")
        TaskStatus status,

        @Schema(description = "사업명")
        String businessName,

        @Schema(description = "발주처")
        String issuingAgency,

        @Schema(description = "연구과제지원")
        TaskSupportType supportType,

        @Schema(description = "삼책오공")
        Boolean threeFiveRule,

        @Schema(description = "과제 시작일")
        LocalDate startDate,

        @Schema(description = "과제 종료일")
        LocalDate endDate,

        @Schema(description = "총 연차")
        Integer totalYears,

        @Schema(description = "현재 연차")
        Integer currentYear,

        @Schema(description = "주관기관")
        String leadInstitution,

        @Schema(description = "담당교수")
        String leadProfessor,

        @Schema(description = "SNUH PI")
        String snuhPi,

        @Schema(description = "김광수교수님 역할")
        TaskProfessorRole professorRole,

        @Schema(description = "실무 책임자 이름")
        String practicalManagerName,

        @Schema(description = "참여기관")
        String participatingInstitutions
) {
    public static TaskSummaryResponse from(Task task) {
        return new TaskSummaryResponse(
                task.getId(),
                task.getResearchTaskNumber(),
                task.getTitle(),
                task.getRfpNumber(),
                task.getRfpName(),
                task.getStatus(),
                task.getBusinessName(),
                task.getIssuingAgency(),
                task.getSupportType(),
                task.getThreeFiveRule(),
                task.getStartDate(),
                task.getEndDate(),
                task.getTotalYears(),
                task.getCurrentYear(),
                task.getLeadInstitution(),
                task.getLeadProfessor(),
                task.getSnuhPi(),
                task.getProfessorRole(),
                task.getPracticalManager().getName(),
                task.getParticipatingInstitutions()
        );
    }
}
