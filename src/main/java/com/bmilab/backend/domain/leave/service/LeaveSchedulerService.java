package com.bmilab.backend.domain.leave.service;

import com.bmilab.backend.domain.leave.dto.response.external.DataPortalHolidayResponse;
import com.bmilab.backend.domain.leave.exception.LeaveErrorCode;
import com.bmilab.backend.domain.leave.repository.UserLeaveRepository;
import com.bmilab.backend.global.exception.ApiException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaveSchedulerService {
    private final LeaveService leaveService;
    @Value("${data-portal.service-key}")
    private String serviceKey;
    private static final String API_URL = "https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo";
    private final UserLeaveRepository userLeaveRepository;

    //TODO: 입사일 기준 근무일 계산으로 다시 바꾸기
    @Scheduled(cron = "0 0 0 1 * *", zone = "Asia/Seoul")
    @Transactional
    public void updateUserAnnualLeaves() {
        YearMonth lastMonthWithYear = YearMonth.now().minusMonths(1);

        if (lastMonthWithYear.getYear() == 2025) {
            return;
        }

        int endOfMonth = lastMonthWithYear.lengthOfMonth();
        int holidayCount = countHolidays(lastMonthWithYear);
        int weekendCount = (int) countWeekends(lastMonthWithYear);
        int weekdayCount = endOfMonth - holidayCount - weekendCount;

        log.info("lastMonthWithYear: {} {}", lastMonthWithYear.getYear(), lastMonthWithYear.getMonth());
        log.info("endOfMonth: {}", endOfMonth);
        log.info("holidayCount: {}", holidayCount);
        log.info("weekendCount: {}", weekendCount);
        log.info("weekdayCount: {}", weekdayCount);

        userLeaveRepository.findAll()
                .forEach((userLeave) -> {
                    long userId = userLeave.getUser().getId();

                    //매해 연가 개수와 사용한 휴가 수 초기화
                    if (lastMonthWithYear.getMonth().equals(Month.DECEMBER)) {
                        log.info("[user={}] ", userId);
                        userLeave.resetLeaveCounts();
                    }

                    int monthLeaveCount = leaveService.countDatesByUserIdWithMonth(userId, lastMonthWithYear);
                    log.info("[user={}] monthLeaveCount: {}", userId, monthLeaveCount);
                    int workedCount = weekdayCount - monthLeaveCount;
                    log.info("[user={}] workedCount: {}", userId, workedCount);
                    int leaveIncrement = 0;

                    if (lastMonthWithYear.getMonth().equals(Month.MAY) || lastMonthWithYear.getMonth().equals(Month.NOVEMBER)) {
                        log.info("[user={}] June, December Increment", userId);
                        leaveIncrement += 1;
                    }

                    if (((weekdayCount * 4) / 5) <= workedCount) {
                        log.info("[user={}] Worked", userId);
                        leaveIncrement += 1;
                    }

                    log.info("[user={}] leaveIncrement: {}", userId, leaveIncrement);
                    userLeave.increaseAnnualLeaveCount((double) leaveIncrement);
                });
    }


    private int countHolidays(YearMonth yearMonth) {
        RestClient restClient = RestClient.create(API_URL);
        int year = yearMonth.getYear();
        String month = String.format("%02d", yearMonth.getMonthValue());

        DataPortalHolidayResponse holidayResponse = Optional.ofNullable(
                restClient.get()
                        .uri((uriBuilder) -> uriBuilder
                                .queryParam("serviceKey", serviceKey)
                                .queryParam("solYear", year)
                                .queryParam("solMonth", month)
                                .queryParam("_type", "json")
                                .build()
                        )
                        .retrieve()
                        .body(DataPortalHolidayResponse.class)
        ).orElseThrow(() -> new ApiException(LeaveErrorCode.HOLIDAY_API_ERROR));

        return (int) holidayResponse
                .response()
                .body()
                .items()
                .item()
                .stream()
                .filter((item) -> !isWeekend(
                        LocalDate.parse(
                                String.valueOf(item.locdate()),
                                DateTimeFormatter.ofPattern("yyyyMMdd")
                        ))
                )
                .count();
    }

    private long countWeekends(YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        return IntStream.rangeClosed(0, yearMonth.lengthOfMonth() - 1)
                .mapToObj(startDate::plusDays)
                .filter(this::isWeekend)
                .count();
    }

    private boolean isWeekend(LocalDate date) {
        return isSaturday(date) || isSunday(date);
    }

    private boolean isSaturday(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY;
    }

    private boolean isSunday(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
