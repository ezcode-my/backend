package org.ezcode.codetest.infrastructure.ranking.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ezcode.codetest.domain.submission.repository.UserProblemResultRepository;
import org.ezcode.codetest.infrastructure.ranking.redis.RedisRankingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankingSyncScheduler {

    private final RedisRankingService redisRankingService;
    private final UserProblemResultRepository resultRepository;

    @Scheduled(cron = "0 0 * * * *") // 매시 정각
    public void syncRankingsToRedis() {
        LocalDate thisMonday = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate lastMonday = thisMonday.minusWeeks(1);

        LocalDateTime thisWeekStart = thisMonday.atStartOfDay();
        LocalDateTime lastWeekStart = lastMonday.atStartOfDay();
        LocalDateTime lastWeekEnd = thisWeekStart;

        // 지난주 랭킹 계산
        List<Object[]> lastWeek = resultRepository.findScoresBetween(lastWeekStart, lastWeekEnd);
        Map<Long, Double> lastWeekMap = lastWeek.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Number) row[1]).doubleValue()
                ));
        redisRankingService.writeWeeklyRanking(lastWeekMap, lastMonday);

        // 이번주 랭킹 계산
        List<Object[]> thisWeek = resultRepository.findScoresBetween(thisWeekStart, LocalDateTime.now());
        Map<Long, Double> thisWeekMap = thisWeek.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Number) row[1]).doubleValue()
                ));
        redisRankingService.writeWeeklyRanking(thisWeekMap, thisMonday);


        // 역대 랭킹 계산
        List<Object[]> allTime = resultRepository.findScoresBetween(null, null);
        Map<Long, Double> allMap = allTime.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Number) row[1]).doubleValue()
                ));
        redisRankingService.writeAllTimeRanking(allMap);

        log.info("Redis 랭킹 갱신 완료: {}", LocalDateTime.now());
    }
}
