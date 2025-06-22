package org.ezcode.codetest.presentation.ranking;

import lombok.RequiredArgsConstructor;
import org.ezcode.codetest.application.ranking.dto.PointResponse;
import org.ezcode.codetest.application.ranking.service.PointService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;

    @GetMapping("/me")
    public ResponseEntity<PointResponse> getMyPoint(@AuthenticationPrincipal AuthUser authUser) {
        Long userId = authUser.getId();
        int totalPoints = pointService.getTotalPoints(userId);
        return ResponseEntity.ok(new PointResponse(totalPoints));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PointResponse> getUserPoint(@PathVariable Long userId) {
        int totalPoint = pointService.getTotalPoints(userId);
        return ResponseEntity.ok(new PointResponse(totalPoint));
    }
}
