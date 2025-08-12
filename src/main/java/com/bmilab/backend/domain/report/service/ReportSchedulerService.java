package com.bmilab.backend.domain.report.service;

import com.bmilab.backend.global.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportSchedulerService {
    private final ReportExcelService reportExcelService;
    private final EmailSender emailSender;

    @Value("${service.professor-mail-address}")
    private String professorMailAddress;

    @Scheduled(cron = "0 0 9 * * MON-FRI", zone = "Asia/Seoul")
    public void sendReportMail() throws IOException {
        //월요일 -> 금요일꺼 나머지는 전날 꺼
        LocalDate today = LocalDate.now();
        LocalDate reportDay = today.minusDays(1);

        if (today.getDayOfWeek() == DayOfWeek.MONDAY) {
            reportDay = today.minusDays(3);
        }

        ByteArrayInputStream excelFile = reportExcelService.getReportExcelFileByDateAsBytes(reportDay);
        emailSender.sendReportEmailAsync(professorMailAddress, reportDay, excelFile);
    }
}
