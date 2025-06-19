package org.ezcode.codetest.infrastructure.persistence.repository.submission.query;

import java.time.LocalDateTime;
import java.util.List;

import org.ezcode.codetest.domain.submission.dto.WeeklySolveCount;

public interface SubmissionQueryRepository {
	List<WeeklySolveCount> fetchWeeklySolveCounts(
		LocalDateTime startDateTime, LocalDateTime endDateTime
	);
}
