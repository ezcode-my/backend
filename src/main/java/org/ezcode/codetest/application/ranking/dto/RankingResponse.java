package org.ezcode.codetest.application.ranking.dto;

import lombok.Builder;

@Builder
public record RankingResponse(
        Long userId,
        String nickname, // 사용자 이름
        int rank,
        int score
) {}
