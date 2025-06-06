package org.ezcode.codetest.domain.problem.model.entity;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.submission.model.entity.Language;
import org.ezcode.codetest.domain.user.model.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "error_note",
	uniqueConstraints = {
	@UniqueConstraint(name = "uk_user_problem", columnNames = {"user_id", "problem_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorNote extends BaseEntity {

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

	@Column(name = "submitted_code", nullable = false, columnDefinition = "longtext")
	private String submittedCode;

	@Column(name = "error_reason", nullable = false, columnDefinition = "text")
	private String errorReason;

	@Column(name = "is_fixed", nullable = false)
	private boolean isFixed;

	@Builder
	public ErrorNote(User user, Problem problem, Language language, String submittedCode,
		String errorReason, boolean isFixed) {
		this.user = user;
		this.problem = problem;
		this.language = language;
		this.submittedCode = submittedCode;
		this.errorReason = errorReason;
		this.isFixed = isFixed;
	}
}
