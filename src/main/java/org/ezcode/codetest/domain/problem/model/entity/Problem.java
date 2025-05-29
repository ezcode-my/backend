package org.ezcode.codetest.domain.problem.model.entity;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.model.enums.Difficulty;
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
	@JoinColumn(nullable = false)
	private User creator;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Category category;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	@Column(nullable = false)
	private int score;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Difficulty difficulty;

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
	public Problem(User creator, Category category, String title, String content, int score, Difficulty difficulty,
		String memoryLimit, int timeLimit, Reference reference) {
		this.creator = creator;
		this.category = category;
		this.title = title;
		this.content = content;
		this.score = score;
		this.difficulty = difficulty;
		this.memoryLimit = memoryLimit;
		this.timeLimit = timeLimit;
		this.reference = reference;
	}
}
