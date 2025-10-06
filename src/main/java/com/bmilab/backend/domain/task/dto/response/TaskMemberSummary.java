package com.bmilab.backend.domain.task.dto.response;

import com.bmilab.backend.domain.task.entity.TaskPresentationMaker;
import com.bmilab.backend.domain.task.entity.TaskProposalWriter;
import io.swagger.v3.oas.annotations.media.Schema;

public record TaskMemberSummary(
        @Schema(description = "사용자 ID")
        Long userId,

        @Schema(description = "이름")
        String name,

        @Schema(description = "이메일")
        String email
) {
    public static TaskMemberSummary fromProposalWriter(TaskProposalWriter writer) {
        return new TaskMemberSummary(
                writer.getUser().getId(),
                writer.getUser().getName(),
                writer.getUser().getEmail()
        );
    }

    public static TaskMemberSummary fromPresentationMaker(TaskPresentationMaker maker) {
        return new TaskMemberSummary(
                maker.getUser().getId(),
                maker.getUser().getName(),
                maker.getUser().getEmail()
        );
    }
}
