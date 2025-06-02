package org.ezcode.codetest.domain.problem.model.entity;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.model.enums.Reference;
import org.ezcode.codetest.domain.user.model.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Problem extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "creator_id", nullable = false)
	private User creator;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Category category;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "LONGTEXT", nullable = false)
	private String description;

	@Column(nullable = false)
	private int score;

	@Column(nullable = false)
	private String difficulty;

	@Column(nullable = false)
	private String memoryLimit;

	@Column(nullable = false)
	private int timeLimit;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Reference reference;

	@Column(nullable = false)
	private Boolean isDeleted;

	@Builder
	public Problem(User creator, Category category, String title, String description, int score, String difficulty,
		String memoryLimit, int timeLimit, Reference reference) {
		this.creator = creator;
		this.category = category;
		this.title = title;
		this.description = description;
		this.score = score;
		this.difficulty = difficulty;
		this.memoryLimit = memoryLimit;
		this.timeLimit = timeLimit;
		this.reference = reference;
		isDeleted = false;
	}

	// 여러개를 하나의 객체로 만드는 것
	public static Problem of(User creator, Category category, String title, String description, int score, String difficulty,
		String memoryLimit, int timeLimit, Reference reference) {

		return Problem.builder()
			.creator(creator)
			.category(category)
			.title(title)
			.description(description)
			.score(score)
			.difficulty(difficulty)
			.memoryLimit(memoryLimit)
			.timeLimit(timeLimit)
			.reference(reference)
			.build();
	}

	// 문제 수정 로직
	public void update(User creator, Category category, String title, String description, String difficulty,
		int score, String memoryLimit, int timeLimit, Reference reference) {

		this.creator = creator;
		this.category = category;
		this.title = title;
		this.description = description;
		this.difficulty = difficulty;
		this.score = score;
		this.memoryLimit = memoryLimit;
		this.timeLimit = timeLimit;
		this.reference = reference;
	}
	// problem id 받아서 problem 반환
	// problem 받아서 testcase 반환 domainService
}
