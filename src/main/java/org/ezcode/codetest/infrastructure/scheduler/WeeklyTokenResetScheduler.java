package org.ezcode.codetest.infrastructure.scheduler;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.TimeZone;

import org.ezcode.codetest.application.usermanagement.user.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WeeklyTokenResetScheduler {

    private final TaskScheduler scheduler;
    private final UserService userService;

    public WeeklyTokenResetScheduler(
        @Qualifier("appTaskScheduler") TaskScheduler scheduler,
        UserService userService
    ) {
        this.scheduler = scheduler;
        this.userService = userService;
    }

    @PostConstruct
    public void schedule() {
        CronTrigger trigger = new CronTrigger(
            "0 0 3 * * MON",
            TimeZone.getTimeZone("Asia/Seoul")
        );

        scheduler.schedule(() -> {
            try {
                log.info("주간 토큰 리셋을 시작합니다.");
                LocalDate lastMonday = LocalDate.now(ZoneId.of("Asia/Seoul"))
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDateTime startDateTime = lastMonday.atStartOfDay();
                LocalDateTime endDateTime = lastMonday.plusDays(7).atStartOfDay();

                userService.resetAllUsersTokensWeekly(startDateTime, endDateTime);
                log.info("주간 토큰 리셋을 성공적으로 완료했습니다.");
            } catch (Exception e) {
                log.error("주간 토큰 리셋에 실패했습니다.", e);
            }
        }, trigger);
    }
}
