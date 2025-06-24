package org.ezcode.codetest.application.ranking.service;

import lombok.RequiredArgsConstructor;
import org.ezcode.codetest.application.ranking.dto.RankingResponse;
import org.ezcode.codetest.application.ranking.dto.TargetRankingResponse;
import org.ezcode.codetest.infrastructure.ranking.redis.RedisRankingService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingFacadeService {

    private final RedisRankingService redisRankingService;

    public List<RankingResponse> getWeeklyRanking() {
        LocalDate thisMonday = LocalDate.now().with(DayOfWeek.MONDAY);
        return redisRankingService.getTopRankings("ranking:weekly:" + thisMonday, 10);
    }

    public List<RankingResponse> getLastWeekRanking() {
        LocalDate lastMonday = LocalDate.now().with(DayOfWeek.MONDAY).minusWeeks(1);
        return redisRankingService.getTopRankings("ranking:weekly:" + lastMonday, 10);
    }

    public List<RankingResponse> getAllTimeRanking() {
        return redisRankingService.getTopRankings("ranking:all", 10);
    }

    //랭킹 조회
    public List<TargetRankingResponse> getRanking(String period, Long userId) {
        LocalDate baseMonday = LocalDate.now().with(DayOfWeek.MONDAY);
        String key = switch (period) {
            case "weekly" -> "ranking:weekly:" + baseMonday;
            case "last-week" -> "ranking:weekly:" + baseMonday.minusWeeks(1);
            case "all-time" -> "ranking:all";
            default -> throw new IllegalArgumentException("기간은 weekly, last-week, all-time 만 가능합니다.");
        };

        return redisRankingService.getRanking(key, userId);
    }

}