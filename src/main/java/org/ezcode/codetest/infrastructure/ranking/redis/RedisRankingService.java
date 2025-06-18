package org.ezcode.codetest.infrastructure.ranking.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ezcode.codetest.application.ranking.dto.RankingResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisRankingService {

    private final StringRedisTemplate redisTemplate;

    private static final String WEEKLY_PREFIX = "ranking:weekly:";
    private static final String ALLTIME_KEY = "ranking:all";

    public void writeWeeklyRanking(Map<Long, Double> scoreMap, LocalDate monday) {
        String key = WEEKLY_PREFIX + monday;
        redisTemplate.delete(key);

        scoreMap.forEach((userId, score) ->
                redisTemplate.opsForZSet().add(key, userId.toString(), score)
        );
        redisTemplate.expire(key, Duration.ofDays(8));
    }

    public void writeAllTimeRanking(Map<Long, Double> scoreMap) {
        redisTemplate.delete(ALLTIME_KEY);

        scoreMap.forEach((userId, score) ->
                redisTemplate.opsForZSet().add(ALLTIME_KEY, userId.toString(), score)
        );
    }

    public List<RankingResponse> getTopRankings(String key, int limit) {
        Set<ZSetOperations.TypedTuple<String>> raw = redisTemplate.opsForZSet()
                .reverseRangeWithScores(key, 0, limit - 1);

        if (raw == null) return List.of();

        int[] rank = {1};
        return raw.stream().map(tuple -> RankingResponse.builder()
                .userId(Long.parseLong(tuple.getValue()))
                .nickname("유저닉네임")
                .rank(rank[0]++)
                .score(tuple.getScore().intValue())
                .build()
        ).collect(Collectors.toList());
    }
}
