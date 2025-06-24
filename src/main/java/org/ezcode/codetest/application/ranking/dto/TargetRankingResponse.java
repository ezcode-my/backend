package org.ezcode.codetest.application.ranking.dto;

public record TargetRankingResponse(
        Long userId,
        String nickname,
        int ranks,
        int score,
        boolean isMe // 프론트에서 이걸보고 누가 특정 사람인지 파악(ex, 색 더 진하게)
) {}
