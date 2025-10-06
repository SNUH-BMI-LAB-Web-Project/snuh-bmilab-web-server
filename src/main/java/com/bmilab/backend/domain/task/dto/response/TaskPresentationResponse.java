package com.bmilab.backend.domain.task.dto.response;

import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.task.entity.TaskPresentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record TaskPresentationResponse(
        @Schema(description = "발표자료 제출 마감일")
        LocalDateTime presentationDeadline,

        @Schema(description = "발표자료 제작 연구원 목록")
        List<TaskMemberSummary> presentationMakers,

        @Schema(description = "최종 발표자료 파일")
        List<FileSummary> finalPresentationFiles,

        @Schema(description = "작성 중 발표자료 파일")
        List<FileSummary> draftPresentationFiles,

        @Schema(description = "발표평가일시")
        LocalDateTime presentationDate,

        @Schema(description = "발표자 이름")
        String presenter,

        @Schema(description = "배석 가능 인원")
        Integer attendeeLimit,

        @Schema(description = "실제 배석자 명단 (콤마로 구분)")
        String attendees,

        @Schema(description = "발표평가장 위치")
        String presentationLocation
) {
    public static TaskPresentationResponse from(
            TaskPresentation presentation,
            List<TaskMemberSummary> makers,
            List<FileSummary> finalPresentationFiles,
            List<FileSummary> draftPresentationFiles
    ) {
        return new TaskPresentationResponse(
                presentation != null ? presentation.getPresentationDeadline() : null,
                makers,
                finalPresentationFiles,
                draftPresentationFiles,
                presentation != null ? presentation.getPresentationDate() : null,
                presentation != null ? presentation.getPresenter() : null,
                presentation != null ? presentation.getAttendeeLimit() : null,
                presentation != null ? presentation.getAttendees() : null,
                presentation != null ? presentation.getPresentationLocation() : null
        );
    }
}
