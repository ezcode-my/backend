package org.ezcode.codetest.application.submission.dto.response.submission;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.submission.model.entity.Submission;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;

@Getter
@JsonPropertyOrder({ "problemId", "problemDescription", "submissions" })
public class GroupedSubmissionResponse {

	private final Long problemId;

	private final String problemDescription;

	private final List<SubmissionDetailResponse> submissions;

	public GroupedSubmissionResponse(Problem problem, List<Submission> submissions) {
		this.problemId = problem.getId();
		this.problemDescription = problem.getDescription();
		this.submissions = submissions.stream()
			.map(SubmissionDetailResponse::from)
			.toList();
	}
}
