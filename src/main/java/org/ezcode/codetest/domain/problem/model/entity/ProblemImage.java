package org.ezcode.codetest.domain.problem.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProblemImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 양방향 연관관계를 위한 setter
	@Setter
	@ManyToOne
	@JoinColumn(name = "problem_id", nullable = false)
	private Problem problem;

	@Column(columnDefinition = "text", nullable = false)
	private String imageUrl;

	public ProblemImage(Problem problem, String imageUrl) {
		this.problem = problem;
		this.imageUrl = imageUrl;
	}
}
