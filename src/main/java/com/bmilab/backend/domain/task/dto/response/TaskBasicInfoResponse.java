package com.bmilab.backend.domain.task.dto.response;

import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.task.entity.TaskBasicInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record TaskBasicInfoResponse(
        @Schema(description = "소관부처")
        String ministry,

        @Schema(description = "전문기관")
        String specializedAgency,

        @Schema(description = "공고번호")
        String announcementNumber,

        @Schema(description = "공고 시작일")
        LocalDate announcementStartDate,

        @Schema(description = "공고 종료일")
        LocalDate announcementEndDate,

        @Schema(description = "사업담당자 이름")
        String businessContactName,

        @Schema(description = "사업담당자 소속")
        String businessContactDepartment,

        @Schema(description = "사업담당자 이메일")
        String businessContactEmail,

        @Schema(description = "사업담당자 전화번호")
        String businessContactPhone,

        @Schema(description = "공고 링크")
        String announcementLink,

        @Schema(description = "3책5공")
        Boolean threeFiveRule,

        @Schema(description = "과제제안요구서(RFP) 파일")
        List<FileSummary> rfpFiles,

        @Schema(description = "공고서류 전체 정보 파일")
        List<FileSummary> announcementFiles
) {
    public static TaskBasicInfoResponse from(TaskBasicInfo basicInfo, LocalDate announcementStartDate, LocalDate announcementEndDate, Boolean threeFiveRule, List<FileSummary> rfpFiles, List<FileSummary> announcementFiles) {
        return new TaskBasicInfoResponse(
                basicInfo != null ? basicInfo.getMinistry() : null,
                basicInfo != null ? basicInfo.getSpecializedAgency() : null,
                basicInfo != null ? basicInfo.getAnnouncementNumber() : null,
                announcementStartDate,
                announcementEndDate,
                basicInfo != null ? basicInfo.getBusinessContactName() : null,
                basicInfo != null ? basicInfo.getBusinessContactDepartment() : null,
                basicInfo != null ? basicInfo.getBusinessContactEmail() : null,
                basicInfo != null ? basicInfo.getBusinessContactPhone() : null,
                basicInfo != null ? basicInfo.getAnnouncementLink() : null,
                threeFiveRule,
                rfpFiles,
                announcementFiles
        );
    }
}
