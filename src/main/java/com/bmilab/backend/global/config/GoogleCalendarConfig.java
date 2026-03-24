package com.bmilab.backend.global.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.List;

@Slf4j
@Getter
@Configuration
public class GoogleCalendarConfig {

    @Value("${google.calendar.service-account-key:}")
    private String serviceAccountKey;

    @Value("${google.calendar.seminar-calendar-id:}")
    private String seminarCalendarId;

    @Value("${google.calendar.leave-calendar-id:}")
    private String leaveCalendarId;

    @Bean
    public Calendar googleCalendar() throws GeneralSecurityException, IOException {
        if (serviceAccountKey == null || serviceAccountKey.isBlank()) {
            log.info("Google Calendar 서비스 계정 키가 설정되지 않아 Calendar Bean을 생성하지 않습니다.");
            return null;
        }

        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new ByteArrayInputStream(serviceAccountKey.getBytes(StandardCharsets.UTF_8)))
                .createScoped(List.of(CalendarScopes.CALENDAR));

        log.info("Google Calendar Bean 생성 완료");

        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("BMI Lab Web Server").build();
    }
}
