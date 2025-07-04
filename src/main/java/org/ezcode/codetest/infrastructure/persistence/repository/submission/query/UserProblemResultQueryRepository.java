package org.ezcode.codetest.infrastructure.persistence.repository.submission.query;

import java.util.List;

import org.ezcode.codetest.domain.submission.dto.DailyCorrectCount;

public interface UserProblemResultQueryRepository {
    List<DailyCorrectCount> countCorrectByUserGroupedByDate(Long userId);
}
