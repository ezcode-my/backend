package org.ezcode.codetest.infrastructure.persistence.repository.submission.impl;

import java.util.List;

import org.ezcode.codetest.domain.submission.model.entity.Submission;
import org.ezcode.codetest.domain.submission.repository.SubmissionRepository;
import org.ezcode.codetest.infrastructure.persistence.repository.submission.jpa.SubmissionJpaRepository;
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

	@Override
	public List<Submission> findSubmissionsGroupedAndSorted(Long userId) {
		return submissionJpaRepository.findAllByUser_Id(userId);
	}
}
