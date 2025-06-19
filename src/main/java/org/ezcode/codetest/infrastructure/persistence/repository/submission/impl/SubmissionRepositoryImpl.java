package org.ezcode.codetest.infrastructure.persistence.repository.submission.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.ezcode.codetest.domain.submission.dto.WeeklySolveCount;
import org.ezcode.codetest.domain.submission.model.entity.Submission;
import org.ezcode.codetest.domain.submission.repository.SubmissionRepository;
import org.ezcode.codetest.infrastructure.persistence.repository.submission.jpa.SubmissionJpaRepository;
import org.ezcode.codetest.infrastructure.persistence.repository.submission.query.SubmissionQueryRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SubmissionRepositoryImpl implements SubmissionRepository {

	private final SubmissionJpaRepository submissionJpaRepository;
	private final SubmissionQueryRepository submissionQueryRepository;

	@Override
	public void saveSubmission(Submission submission) {
		submissionJpaRepository.save(submission);
	}

	@Override
	public List<Submission> findSubmissionsByUserId(Long userId) {
		return submissionJpaRepository.findAllByUser_Id(userId);
	}

	@Override
	public List<WeeklySolveCount> fetchWeeklySolveCounts(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		return submissionQueryRepository.fetchWeeklySolveCounts(startDateTime, endDateTime);
	}
}
