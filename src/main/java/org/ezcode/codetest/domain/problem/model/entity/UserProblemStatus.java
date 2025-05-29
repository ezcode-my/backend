package org.ezcode.codetest.domain.submission.model;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_problem_status")
@NoArgsConstructor
public class UserProblemStatus extends BaseEntity {

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
	private boolean status;

	@Builder
	public UserProblemStatus(User user, Problem problem, boolean status) {
		this.user = user;
		this.problem = problem;
		this.status = status;
	}
}
