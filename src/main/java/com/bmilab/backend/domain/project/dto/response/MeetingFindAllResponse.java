package com.bmilab.backend.domain.project.dto.response;

import com.bmilab.backend.domain.project.entity.Meeting;
import com.bmilab.backend.domain.project.enums.MeetingType;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
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
            Long meetingId,
            Long projectId,
            UserSummary recorder,
            String title,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            MeetingType meetingType,
            String summary,
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
