package com.bmilab.backend.domain.task.dto.response;

import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.task.entity.TaskProposal;
import com.bmilab.backend.domain.task.entity.TaskProposalWriter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record TaskProposalResponse(
        @Schema(description = "제안서 제출 마감일")
        LocalDateTime proposalDeadline,

        @Schema(description = "제안서 작성 연구원 목록")
        List<ProposalWriter> proposalWriters,

        @Schema(description = "발주처 담당자 이름")
        String contractorContactName,

        @Schema(description = "발주처 담당자 부서")
        String contractorContactDepartment,

        @Schema(description = "발주처 담당자 이메일")
        String contractorContactEmail,

        @Schema(description = "발주처 담당자 전화번호")
        String contractorContactPhone,

        @Schema(description = "원내 담당자 이름")
        String internalContactName,

        @Schema(description = "원내 담당자 부서")
        String internalContactDepartment,

        @Schema(description = "원내 담당자 이메일")
        String internalContactEmail,

        @Schema(description = "원내 담당자 전화번호")
        String internalContactPhone,

        @Schema(description = "최종 제안서 파일")
        List<FileSummary> finalProposalFiles,

        @Schema(description = "최종 제출서류 파일")
        List<FileSummary> finalSubmissionFiles,

        @Schema(description = "관련 파일 목록")
        List<FileSummary> relatedFiles,

        @Schema(description = "제안서 작성 회의록 파일")
        List<FileSummary> meetingNotesFiles,

        @Schema(description = "과제 구성 모식도 파일 (이미지/PDF)")
        List<FileSummary> structureDiagramFiles
) {

    public static TaskProposalResponse from(
            TaskProposal proposal,
            List<ProposalWriter> writers,
            List<FileSummary> finalProposalFiles,
            List<FileSummary> finalSubmissionFiles,
            List<FileSummary> relatedFiles,
            List<FileSummary> meetingNotesFiles,
            List<FileSummary> structureDiagramFiles
    ) {

        return new TaskProposalResponse(
                proposal != null ? proposal.getProposalDeadline() : null,
                writers,
                proposal != null ? proposal.getContractorContactName() : null,
                proposal != null ? proposal.getContractorContactDepartment() : null,
                proposal != null ? proposal.getContractorContactEmail() : null,
                proposal != null ? proposal.getContractorContactPhone() : null,
                proposal != null ? proposal.getInternalContactName() : null,
                proposal != null ? proposal.getInternalContactDepartment() : null,
                proposal != null ? proposal.getInternalContactEmail() : null,
                proposal != null ? proposal.getInternalContactPhone() : null,
                finalProposalFiles,
                finalSubmissionFiles,
                relatedFiles,
                meetingNotesFiles,
                structureDiagramFiles
        );
    }

    public record ProposalWriter(
            @Schema(description = "작성자 ID")
            Long userId,

            @Schema(description = "작성자 이름")
            String name,

            @Schema(description = "작성자 이메일")
            String email
    ) {
        public static ProposalWriter from(TaskProposalWriter writer) {
            return new ProposalWriter(
                    writer.getUser().getId(),
                    writer.getUser().getName(),
                    writer.getUser().getEmail()
            );
        }
    }
}
