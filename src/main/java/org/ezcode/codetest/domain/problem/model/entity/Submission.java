package org.ezcode.codetest.domain.submission.model;

import java.time.LocalDateTime;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.problem.model.entity.Language;
import org.springframework.data.annotation.CreatedDate;
import org.ezcode.codetest.domain.user.model.User;
import org.ezcode.codetest.domain.problem.model.entity.Problem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Submission extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "problem_id", nullable = false)
	private Problem problem;

	@ManyToOne
	@JoinColumn(name = "language_id", nullable = false)
	private Language language;

	@Column(nullable = false, columnDefinition = "longtext")
	private String code;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SubmitStatus status;

	@Column(name = "testcase_passed_count", nullable = false)
	private int testCasePassedCount;

	@Column(name = "testcase_total_count", nullable = false)
	private int testCaseTotalCount;

	@Column(name = "execution_time", nullable = false)
	private int executionTime;

	@Column(name = "memory_usage", nullable = false)
	private int memoryUsage;

	@CreatedDate
	@Column(name = "submitted_at", nullable = false)
	private LocalDateTime submittedAt;

	@Builder
	public Submission(User user, Problem problem, Language language, String code, SubmitStatus status,
		int testCasePassedCount, int testCaseTotalCount, int executionTime, int memoryUsage, LocalDateTime submittedAt
		) {
		this.user = user;
		this.problem = problem;
		this.language = language;
		this.code = code;
		this.status = status;
		this.testCasePassedCount = testCasePassedCount;
		this.testCaseTotalCount = testCaseTotalCount;
		this.executionTime = executionTime;
		this.memoryUsage = memoryUsage;
		this.submittedAt = submittedAt;
	}
}
