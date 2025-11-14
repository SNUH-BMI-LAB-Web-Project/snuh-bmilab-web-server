package com.bmilab.backend.domain.report.service;

import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.repository.ProjectRepository;
import com.bmilab.backend.domain.report.dto.query.GetAllReportsQueryResult;
import com.bmilab.backend.domain.report.repository.ReportRepository;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.email.EmailSender;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
//@Profile("prod")
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

    @Value("${service.professor-mail-address}")
    private String professorMailAddress;

    @Scheduled(cron = "0 0 9 * * MON-FRI", zone = "Asia/Seoul")
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

    @Scheduled(cron = "0 20 10 * * MON-FRI", zone = "Asia/Seoul")
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
}
