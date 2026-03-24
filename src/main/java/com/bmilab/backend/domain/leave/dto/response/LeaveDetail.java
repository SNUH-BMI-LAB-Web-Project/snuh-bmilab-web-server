package com.bmilab.backend.domain.leave.dto.response;

import com.bmilab.backend.domain.leave.entity.Leave;
import com.bmilab.backend.domain.leave.enums.LeaveStatus;
import com.bmilab.backend.domain.leave.enums.LeaveType;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import io.swagger.v3.oas.annotations.media.Schema;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;

@Builder
public record LeaveDetail(
        @Schema(description = "휴가 ID", example = "123")
        Long leaveId,

        @Schema(description = "사용자 정보")
        UserSummary user,

        @Schema(description = "휴가 시작 일시")
        LocalDate startDate,

        @Schema(description = "휴가 종료 일시")
        LocalDate endDate,

        @Schema(description = "휴가 승인 상태", example = "APPROVED")
        LeaveStatus status,

        @Schema(description = "휴가 유형", example = "ANNUAL")
        LeaveType type,

        @Schema(description = "휴가 사유", example = "개인 사유")
        String reason,

        @Schema(description = "반려 사유 (있을 경우)", example = "업무 공백 우려로 인해 반려됨")
        String rejectReason,

        @Schema(description = "휴가 처리자 정보")
        UserSummary processor,

        @Schema(description = "처리 일시", example = "2025-04-23T15:30:00")
        LocalDateTime processedAt,

        @Schema(description = "신청 일시", example = "2025-04-23T15:30:00")
        LocalDateTime applicatedAt,

        @Schema(description = "Google 캘린더 추가 링크")
        String googleCalendarLink
) {
    public static LeaveDetail from(Leave leave) {
        String eventTitle = leave.getUser().getName() + " " + leave.getType().getDescription();
        return LeaveDetail
                .builder()
                .leaveId(leave.getId())
                .user(UserSummary.from(leave.getUser()))
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .status(leave.getStatus())
                .type(leave.getType())
                .reason(leave.getReason())
                .rejectReason(leave.getRejectReason())
                .processor(leave.getProcessor() == null ? null : UserSummary.from(leave.getProcessor()))
                .processedAt(leave.getProcessedAt())
                .applicatedAt(leave.getApplicatedAt())
                .googleCalendarLink(buildGoogleCalendarLink(eventTitle, leave.getStartDate(), leave.getEndDate()))
                .build();
    }

    private static String buildGoogleCalendarLink(String title, LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String start = startDate.format(df);
        // AIDEV-NOTE: 종일 이벤트 — endDate는 exclusive이므로 +1일
        LocalDate effectiveEnd = (endDate != null ? endDate : startDate).plusDays(1);
        String end = effectiveEnd.format(df);

        return "https://calendar.google.com/calendar/render?action=TEMPLATE"
                + "&text=" + encodedTitle
                + "&dates=" + start + "/" + end;
    }
}
