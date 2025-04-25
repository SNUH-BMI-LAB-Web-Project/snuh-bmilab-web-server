package com.bmilab.backend.domain.project.dto.response;

import com.bmilab.backend.domain.project.entity.Meeting;
import com.bmilab.backend.domain.project.enums.MeetingType;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;

public record MeetingFindAllResponse(
        List<MeetingSummary> meetings
) {
    public static MeetingFindAllResponse of(List<Meeting> meetings) {
        return new MeetingFindAllResponse(
                meetings
                    .stream()
                    .map(MeetingSummary::from)
                    .toList()
        );
    }

    @Builder
    public record MeetingSummary(
            @Schema(description = "미팅 ID", example = "101")
            Long meetingId,

            @Schema(description = "프로젝트 ID", example = "55")
            Long projectId,

            @Schema(description = "미팅 기록 작성자 정보")
            UserSummary recorder,

            @Schema(description = "미팅 제목", example = "UX 개선 회의")
            String title,

            @Schema(description = "미팅 날짜", example = "2025-05-10")
            LocalDate date,

            @Schema(description = "미팅 시작 시간 (24시간제)", example = "14:00")
            LocalTime startTime,

            @Schema(description = "미팅 종료 시간 (24시간제)", example = "15:30")
            LocalTime endTime,

            @Schema(description = "미팅 유형", example = "RESEARCH_PRESENTATION")
            MeetingType meetingType,

            @Schema(description = "미팅 요약", example = "디자인 시스템 전면 개편 논의 및 일정 정리")
            String summary,

            @Schema(description = "댓글 수", example = "5")
            int commentCount
    ) {
        public static MeetingSummary from(Meeting meeting) {
            return MeetingSummary
                    .builder()
                    .meetingId(meeting.getId())
                    .projectId(meeting.getProject().getId())
                    .recorder(UserSummary.from(meeting.getRecorder()))
                    .title(meeting.getTitle())
                    .date(meeting.getDate())
                    .startTime(meeting.getStartTime())
                    .endTime(meeting.getEndTime())
                    .meetingType(meeting.getType())
                    .summary(meeting.getSummary())
                    //TODO: 댓글 총 개수 구하기
                    .commentCount(2)
                    .build();
        }
    }
}
