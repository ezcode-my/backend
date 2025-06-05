package org.ezcode.codetest.domain.problem.repository;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.Submission;

public interface SubmissionRepository {
	void saveSubmission(Submission submission);

	List<Submission> findSubmissionsByUserId(Long userId);
}
