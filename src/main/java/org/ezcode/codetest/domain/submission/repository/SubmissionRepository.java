package org.ezcode.codetest.domain.submission.repository;

import java.util.List;

import org.ezcode.codetest.domain.submission.model.entity.Submission;

public interface SubmissionRepository {
	void saveSubmission(Submission submission);

	List<Submission> findSubmissionsByUserId(Long userId);

	List<Submission> findSubmissionsGroupedAndSorted(Long userId);
}
