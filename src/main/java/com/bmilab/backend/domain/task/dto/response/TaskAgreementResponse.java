package com.bmilab.backend.domain.task.dto.response;

import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.task.entity.TaskAgreement;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record TaskAgreementResponse(
        @Schema(description = "협약 체결일")
        LocalDate agreementDate,

        @Schema(description = "협약용 최종 제안서 파일")
        List<FileSummary> agreementFinalProposalFiles,

        @Schema(description = "협약용 최종 제출서류 파일")
        List<FileSummary> agreementFinalSubmissionFiles
) {
    public static TaskAgreementResponse from(
            TaskAgreement agreement,
            List<FileSummary> agreementFinalProposalFiles,
            List<FileSummary> agreementFinalSubmissionFiles
    ) {
        return new TaskAgreementResponse(
                agreement != null ? agreement.getAgreementDate() : null,
                agreementFinalProposalFiles,
                agreementFinalSubmissionFiles
        );
    }
}
