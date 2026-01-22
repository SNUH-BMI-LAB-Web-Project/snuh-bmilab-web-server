package com.bmilab.backend.domain.task.dto.response;

import com.bmilab.backend.domain.task.entity.Task;
import com.bmilab.backend.domain.task.enums.TaskProfessorRole;
import com.bmilab.backend.domain.task.enums.TaskStatus;
import com.bmilab.backend.domain.task.enums.TaskSupportType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

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

        @Schema(description = "총 연차")
        Integer totalYears,

        @Schema(description = "현재 연차")
        Integer currentYear,

        @Schema(description = "과제 전체 시작일 (첫 연차의 시작일)")
        LocalDate taskStartDate,

        @Schema(description = "과제 전체 종료일 (마지막 연차의 종료일)")
        LocalDate taskEndDate,

        @Schema(description = "과제 연차별 기간 목록")
        List<TaskPeriodSummaryResponse> periods,

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
        String participatingInstitutions,

        @Schema(description = "원내과제 여부")
        Boolean isInternal
) {
    public static TaskSummaryResponse from(Task task, List<TaskPeriodSummaryResponse> periods) {
        LocalDate calculatedStartDate = null;
        LocalDate calculatedEndDate = null;

        if (!periods.isEmpty()) {
            calculatedStartDate = periods.get(0).startDate();
            calculatedEndDate = periods.get(periods.size() - 1).endDate();
        }

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
                task.getTotalYears(),
                task.getCurrentYear(),
                calculatedStartDate,
                calculatedEndDate,
                periods,
                task.getLeadInstitution(),
                task.getLeadProfessor(),
                task.getSnuhPi(),
                task.getProfessorRole(),
                task.getPracticalManager().getName(),
                task.getParticipatingInstitutions(),
                task.getIsInternal()
        );
    }
}
