package org.ezcode.codetest.presentation.ranking;

import lombok.RequiredArgsConstructor;
import org.ezcode.codetest.application.ranking.dto.PointResponse;
import org.ezcode.codetest.application.ranking.dto.RankingResponse;
import org.ezcode.codetest.application.ranking.dto.TargetRankingResponse;
import org.ezcode.codetest.application.ranking.service.RankingFacadeService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.infrastructure.ranking.scheduler.RankingSyncScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    // 내 랭킹 조회
    // weekly, last-week, all-time 3개 가능 뭘 표시할지 의논해서 쓰면 될듯
    @GetMapping("/me/around")
    public List<TargetRankingResponse> getMyRanking(
            @RequestParam("period") String period,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        return rankingService.getRanking(period, authUser.getId());
    }

    @GetMapping("/{userId}/around")
    public List<TargetRankingResponse> getTargetAroundRanking(
            @RequestParam("period") String period,
            @PathVariable Long userId
    ) {
        return rankingService.getRanking(period, userId);
    }


}
