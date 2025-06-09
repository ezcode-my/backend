package org.ezcode.codetest.domain.submission.model.entity;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.user.model.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

	@Column(nullable = false)
	private String message;

	@Column(name = "testcase_passed_count", nullable = false)
	private int testCasePassedCount;

	@Column(name = "testcase_total_count", nullable = false)
	private int testCaseTotalCount;

	@Column(name = "execution_time", nullable = false)
	private Double executionTime;

	@Column(name = "memory_usage", nullable = false)
	private Long memoryUsage;

	@Builder
	public Submission(User user, Problem problem, Language language, String code, String message,
		int testCasePassedCount, int testCaseTotalCount, Double executionTime, Long memoryUsage) {
		this.user = user;
		this.problem = problem;
		this.language = language;
		this.code = code;
		this.message = message;
		this.testCasePassedCount = testCasePassedCount;
		this.testCaseTotalCount = testCaseTotalCount;
		this.executionTime = executionTime;
		this.memoryUsage = memoryUsage;
	}

	public Long getUserId() {
		return this.user.getId();
	}

	public Long getProblemId() { return this.problem.getId(); }

	public String getProblemDescription() { return this.problem.getDescription(); }

	public boolean isCorrect() {
		return this.testCasePassedCount == this.testCaseTotalCount;
	}
}
