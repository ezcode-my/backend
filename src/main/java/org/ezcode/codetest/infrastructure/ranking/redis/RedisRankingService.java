package org.ezcode.codetest.infrastructure.ranking.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ezcode.codetest.application.ranking.dto.RankingResponse;
import org.ezcode.codetest.application.ranking.dto.TargetRankingResponse;
import org.ezcode.codetest.infrastructure.persistence.repository.user.UserJpaRepository;
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
    private final UserJpaRepository userJpaRepository;


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
        return raw.stream().map(tuple -> {
            Long userId = Long.parseLong(tuple.getValue());
            String nickname = userJpaRepository.findById(userId)
                    .map(user -> user.getNickname())
                    .orElse("알 수 없음");

            return RankingResponse.builder()
                    .userId(userId)
                    .nickname(nickname)
                    .ranks(rank[0]++)
                    .score(tuple.getScore().intValue())
                    .build();
        }).collect(Collectors.toList());
    }

    public List<TargetRankingResponse> getRanking(String key, Long userId) {
        Long myRank = redisTemplate.opsForZSet().reverseRank(key, userId.toString());

        // 랭킹에 없을 경우
        if (myRank == null) {
            long total = Optional.ofNullable(redisTemplate.opsForZSet().zCard(key)).orElse(0L);

            // 점수 있는 가장 꼴찌 유저 조회
            Set<ZSetOperations.TypedTuple<String>> lastOneSet = redisTemplate.opsForZSet()
                    .rangeWithScores(key, 0, 0); // 정방향 range로 가장 낮은 점수 유저
            List<TargetRankingResponse> result = new ArrayList<>();

            if (lastOneSet != null && !lastOneSet.isEmpty()) {
                ZSetOperations.TypedTuple<String> last = lastOneSet.iterator().next();
                Long lastUserId = Long.parseLong(last.getValue());
                int lastScore = last.getScore().intValue();
                String lastNickname = userJpaRepository.findById(lastUserId)
                        .map(u -> u.getNickname())
                        .orElse("알 수 없음");

                Long lastUserRank = redisTemplate.opsForZSet().reverseRank(key, last.getValue());
                result.add(new TargetRankingResponse(
                        lastUserId,
                        lastNickname,
                        lastUserRank != null ? lastUserRank.intValue() + 1 : -1,
                        lastScore,
                        false
                ));
            }

            // 0점인 본인 유저 추가
            String myNickname = userJpaRepository.findById(userId)
                    .map(u -> u.getNickname())
                    .orElse("알 수 없음");

            result.add(new TargetRankingResponse(
                    userId,
                    myNickname,
                    (int) total + 1,
                    0,
                    true
            ));

            return result;
        }

        // 내가 랭킹 안에 있는 경우: 위 1명, 나, 아래 1명 조회
        long start = Math.max(0, myRank - 1);
        long end = myRank + 1;

        Set<ZSetOperations.TypedTuple<String>> range = redisTemplate.opsForZSet()
                .reverseRangeWithScores(key, start, end);

        if (range == null || range.isEmpty()) return List.of();

        List<String> ids = range.stream().map(ZSetOperations.TypedTuple::getValue).toList();
        Map<Long, String> nicknameMap = userJpaRepository.findAllById(
                ids.stream().map(Long::parseLong).toList()
        ).stream().collect(Collectors.toMap(
                u -> u.getId(),
                u -> u.getNickname()
        ));

        return range.stream().map(tuple -> {
            Long id = Long.parseLong(tuple.getValue());
            Long rank = redisTemplate.opsForZSet().reverseRank(key, tuple.getValue());

            return new TargetRankingResponse(
                    id,
                    nicknameMap.getOrDefault(id, "알 수 없음"),
                    rank != null ? rank.intValue() + 1 : -1,
                    tuple.getScore() != null ? tuple.getScore().intValue() : 0,
                    id.equals(userId)
            );
        }).sorted(Comparator.comparingInt(TargetRankingResponse::ranks)).toList();
    }


}
