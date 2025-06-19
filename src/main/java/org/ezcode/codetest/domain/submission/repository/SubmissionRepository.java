package org.ezcode.codetest.domain.submission.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.ezcode.codetest.domain.submission.dto.WeeklySolveCount;
import org.ezcode.codetest.domain.submission.model.entity.Submission;

public interface SubmissionRepository {
	void saveSubmission(Submission submission);

	List<Submission> findSubmissionsByUserId(Long userId);

	List<WeeklySolveCount> fetchWeeklySolveCounts(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
