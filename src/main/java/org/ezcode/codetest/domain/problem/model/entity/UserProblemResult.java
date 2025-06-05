package org.ezcode.codetest.domain.problem.model.entity;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.user.model.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_problem_result")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProblemResult extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "problem_id", nullable = false)
	private Problem problem;

	@Column(nullable = false)
	private boolean isCorrect;

	@Builder
	public UserProblemResult(User user, Problem problem, boolean isCorrect) {
		this.user = user;
		this.problem = problem;
		this.isCorrect = isCorrect;
	}

	public void updateResult(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
}
