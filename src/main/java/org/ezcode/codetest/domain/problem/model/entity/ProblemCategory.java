package org.ezcode.codetest.domain.problem.model.entity;

import static jakarta.persistence.FetchType.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "problem_category",
	uniqueConstraints = @UniqueConstraint(
		name = "uk_problem_category",
		columnNames = {"problem_id", "category_id"}
	)
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProblemCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@ManyToOne(fetch = LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "problem_id", nullable = false)
	private Problem problem;

	@ManyToOne(fetch = LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	public ProblemCategory(Problem problem, Category category) {

		this.problem = problem;
		this.category = category;
	}

	public static ProblemCategory from(Problem problem, Category category) {

		return new ProblemCategory(problem, category);
	}

}
