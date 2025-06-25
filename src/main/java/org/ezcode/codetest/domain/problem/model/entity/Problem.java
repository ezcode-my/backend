package org.ezcode.codetest.domain.problem.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.model.enums.Difficulty;
import org.ezcode.codetest.domain.problem.model.enums.Reference;
import org.ezcode.codetest.domain.user.model.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	private Long memoryLimit;

	@Column(nullable = false)
	private Long timeLimit;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Reference reference;

	@Column(nullable = false)
	private Boolean isDeleted;

	@Column(nullable = false)
	private Long totalSubmissions;

	@Column(nullable = false)
	private Long correctSubmissions;

	@OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Testcase> testcases = new ArrayList<>();

	@ElementCollection(fetch = FetchType.LAZY)
	private List<String> imageUrl = new ArrayList<>();

	@Builder
	public Problem(User creator, Category category, String title, String description, int score, String difficulty,
		Long memoryLimit, Long timeLimit, Reference reference) {
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
		this.totalSubmissions = 0L;
		this.correctSubmissions = 0L;
	}

	// 여러개를 하나의 객체로 만드는 것
	public static Problem of(User creator, Category category, String title, String description, int score, String difficulty,
		Long memoryLimit, Long timeLimit, Reference reference) {

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
	public void update(User creator, Category category, String title, String description, Difficulty difficulty,
		Long memoryLimit, Long timeLimit, Reference reference) {

		if (creator != null) this.creator = creator;
		if (category != null) this.category = category;
		if (title != null) this.title = title;
		if (description != null) this.description = description;
		if (difficulty != null) {
			this.difficulty = difficulty.getDifficulty();
			this.score = difficulty.getScore();
		}
		if (memoryLimit != null)this.memoryLimit = memoryLimit;
		if (timeLimit != null) this.timeLimit = timeLimit;
		if (reference != null) this.reference = reference;
	}

	public void softDelete() {
		this.isDeleted = true;
	}

	// 이미지 추가
	public void addImage(String image) {
		if (image == null || image.trim().isEmpty()) {
			throw new IllegalArgumentException("이미지 URL을 찾을수 없습니다");
		}

		if (imageUrl.contains(image)) {
			return; // 중복된 URL 무시
		}

		imageUrl.add(image);
	}

}
