package com.bmilab.backend.domain.report.service;

import com.bmilab.backend.domain.report.dto.query.GetAllReportsQueryResult;
import com.bmilab.backend.domain.report.repository.ReportRepository;
import com.bmilab.backend.global.email.EmailSender;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@Profile("prod")
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportSchedulerService {
    private final ReportExcelService reportExcelService;
    private final ReportWordService reportWordService;
    private final EmailSender emailSender;
    private final ReportRepository reportRepository;
    private final ReportExportConverter reportExportConverter;

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
        ByteArrayInputStream wordFile  = reportWordService.getReportWordFileByDateAsBytes(reportDay); // ← 이렇게

        emailSender.sendReportEmailAsync(professorMailAddress, reportDay, bodyPlain, excelFile, wordFile);

    }

}
