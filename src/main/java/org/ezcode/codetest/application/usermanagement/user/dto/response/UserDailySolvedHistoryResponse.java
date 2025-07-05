package org.ezcode.codetest.application.usermanagement.user.dto.response;

import java.util.List;

import org.ezcode.codetest.domain.submission.dto.DailyCorrectCount;

public record UserDailySolvedHistoryResponse (
    Long userId,
    List<DailyCorrectCount> dailySolvedCounts
){}
