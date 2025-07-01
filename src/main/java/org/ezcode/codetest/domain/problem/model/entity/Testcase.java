package org.ezcode.codetest.domain.problem.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class Testcase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "problem_id", nullable = false)
	private Problem problem;

	@Column
	private String input;

	@Column
	private String output;

	@Builder
	public Testcase(Long id, Problem problem, String input, String output) {
		this.id = id;
		this.problem = problem;
		this.input = input;
		this.output = output;
	}

	// 테스트 케이스 수정 로직
	public void update(String input, String output) {
		if(input != null) this.input = input;
		if(output != null) this.output = output;
	}

	public boolean problemIdMatched(Long problemId) {
		return this.problem != null &&
				this.problem.getId() != null &&
				this.getProblem().getId().equals(problemId);
	}

	public String getInput() {
		if (input != null) return this.input.replace("\\n", "\n");
		return null;
	}

	public String getOutput() {
		return this.output.replace("\\n", "\n");
	}
}