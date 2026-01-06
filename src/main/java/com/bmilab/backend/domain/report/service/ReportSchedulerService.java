package com.bmilab.backend.domain.report.service;

import com.bmilab.backend.domain.leave.entity.Leave;
import com.bmilab.backend.domain.leave.enums.LeaveStatus;
import com.bmilab.backend.domain.leave.repository.LeaveRepository;
import com.bmilab.backend.domain.project.repository.ProjectRepository;
import com.bmilab.backend.domain.report.dto.query.GetAllReportsQueryResult;
import com.bmilab.backend.domain.report.repository.ReportRepository;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.email.EmailSender;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@Profile("prod")
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportSchedulerService {

    private final ReportExcelService reportExcelService;
    private final ReportWordService reportWordService;
    private final TelegramService telegramService;
    private final EmailSender emailSender;
    private final ReportRepository reportRepository;
    private final ReportExportConverter reportExportConverter;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final LeaveRepository leaveRepository;

    private static final DateTimeFormatter DATE_WITH_DAY_FORMATTER =
            DateTimeFormatter.ofPattern("MM/dd E", Locale.KOREAN);

    @Value("${service.professor-mail-address}")
    private String professorMailAddress;

    @Scheduled(cron = "0 5 9 * * MON-FRI", zone = "Asia/Seoul")
    public void sendReportMail() throws IOException, MessagingException {
        //월요일 -> 금요일꺼 나머지는 전날 꺼
        LocalDate today = LocalDate.now();
        LocalDate reportDay = today.minusDays(1);

        if (today.getDayOfWeek() == DayOfWeek.MONDAY) {
            reportDay = today.minusDays(3);
        }

        List<GetAllReportsQueryResult> results = reportRepository.findAllByDateWithFiles(reportDay);
        String bodyPlain = reportExportConverter.toMailBodyPlain(results);

        ByteArrayInputStream excelFile = reportExcelService.getReportExcelFileByDateAsBytes(reportDay);
        ByteArrayInputStream wordFile = reportWordService.getReportWordFileByDateAsBytes(reportDay);

        emailSender.sendReportEmailAsync(professorMailAddress, reportDay, bodyPlain, excelFile, wordFile);

        String bodyMarkdown = reportExportConverter.toTelegramBodyMarkdown(results);

        String title = "\\[SNUH BMI Lab\\] `" + reportDay + "` 업무일지 보고드립니다\\.";
        telegramService.sendMessage(title + "\n\n" + bodyMarkdown);

    }

    @Scheduled(cron = "0 0 9 * * MON-FRI", zone = "Asia/Seoul")
    public void checkBiWeeklyReportStatus() {
        LocalDate end = LocalDate.now().minusDays(1);
        LocalDate start = end.minusDays(13);

        log.info("[2주 미제출 체크] 조회 기간: {} ~ {}", start, end);

        // 프로젝트 중심 미보고 현황 조회
        var projectsMissingReports = reportRepository.findProjectsMissingReportsInPeriod(start, end);
        log.info("[2주 미제출 체크] 미보고 프로젝트 수: {}", projectsMissingReports.size());

        // 유저별 미보고 현황 조회
        var userProjectsMissingReports = reportRepository.findUserProjectsMissingReportsInPeriod(start, end);
        log.info("[2주 미제출 체크] 유저-프로젝트 미보고 조합 수: {}", userProjectsMissingReports.size());

        // 텔레그램 메시지 생성 및 전송
        String title = "\\[SNUH BMI Lab\\] 2주 보고 미제출 현황 \\(`" + start + "` \\~ `" + end + "`\\)";
        String body = reportExportConverter.toBiWeeklyMissingReports(projectsMissingReports, userProjectsMissingReports);
        telegramService.sendMessage(title + "\n\n" + body);
    }

    @Scheduled(cron = "0 10 9 * * MON-FRI", zone = "Asia/Seoul")
    public void notifyWeeklyLeaves() {
        LocalDate today = LocalDate.now();
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));

        log.info("[금주 휴가자 알림] 조회 기간: {} ~ {}", today, endOfWeek);

        List<Leave> leaves = leaveRepository.findWeeklyLeavesEndingAfterToday(today, endOfWeek, LeaveStatus.APPROVED);
        log.info("[금주 휴가자 알림] 휴가자 수: {}", leaves.size());

        String title = "\\[SNUH BMI Lab\\] 금주 휴가자 현황";
        String body = buildWeeklyLeavesMessage(leaves);

        telegramService.sendMessage(title + "\n\n" + body);
    }

    private String buildWeeklyLeavesMessage(List<Leave> leaves) {
        if (leaves.isEmpty()) {
            return "금주 휴가자가 없습니다\\.";
        }

        StringBuilder sb = new StringBuilder();
        for (Leave leave : leaves) {
            String name = escMdV2(leave.getUser().getName());
            String email = leave.getUser().getEmail();
            String leaveType = escMdV2(leave.getType().getDescription());
            String period = formatLeavePeriod(leave);

            sb.append("\\- ")
              .append(name)
              .append(" \\(")
              .append("`").append(email).append("`")
              .append("\\) \\- ")
              .append(leaveType)
              .append(" \\(")
              .append(period)
              .append("\\)\n");
        }

        return sb.toString().trim();
    }

    private String formatLeavePeriod(Leave leave) {
        String startDateStr = escMdV2(leave.getStartDate().format(DATE_WITH_DAY_FORMATTER));

        if (leave.getEndDate() == null) {
            // 반차인 경우 당일만 표시
            return startDateStr;
        }

        if (leave.getStartDate().equals(leave.getEndDate())) {
            // 연차지만 하루만인 경우
            return startDateStr;
        }

        // 연차 기간 표시
        String endDateStr = escMdV2(leave.getEndDate().format(DATE_WITH_DAY_FORMATTER));
        return startDateStr + " \\~ " + endDateStr;
    }

    // AIDEV-NOTE: MarkdownV2 특수문자 이스케이프 처리
    private static String escMdV2(String s) {
        if (s == null) return "";
        s = s.replace("\\", "\\\\");
        return s.replaceAll("([_*\\[\\]()~`>#+\\-=|{}.!])", "\\\\$1");
    }
}
