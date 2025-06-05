package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.Submission;
import org.ezcode.codetest.domain.problem.repository.SubmissionRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SubmissionRepositoryImpl implements SubmissionRepository {

	private final SubmissionJpaRepository submissionJpaRepository;

	@Override
	public void saveSubmission(Submission submission) {
		submissionJpaRepository.save(submission);
	}

	@Override
	public List<Submission> findSubmissionsByUserId(Long userId) {
		return submissionJpaRepository.findAllByUser_Id(userId);
	}
}
