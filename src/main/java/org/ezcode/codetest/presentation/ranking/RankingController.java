package org.ezcode.codetest.presentation.ranking;

import lombok.RequiredArgsConstructor;
import org.ezcode.codetest.application.ranking.dto.RankingResponse;
import org.ezcode.codetest.application.ranking.service.RankingFacadeService;
import org.ezcode.codetest.infrastructure.ranking.scheduler.RankingSyncScheduler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rankings")
@RequiredArgsConstructor
public class RankingController {

    private final RankingFacadeService rankingService;
    private final RankingSyncScheduler rankingSyncScheduler;


    @GetMapping("/weekly")
    public List<RankingResponse> getWeekly() {
        return rankingService.getWeeklyRanking();
    }

    @GetMapping("/last-week")
    public List<RankingResponse> getLastWeek() {
        return rankingService.getLastWeekRanking();
    }

    @GetMapping("/all-time")
    public List<RankingResponse> getAllTime() {
        return rankingService.getAllTimeRanking();
    }

    @PostMapping("/refresh-rankings")
    public void forceRefreshRankings() {
        rankingSyncScheduler.syncRankingsToRedis();
    }
}
