package org.ezcode.codetest.domain.community.model.entity;

import java.util.Objects;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.user.model.entity.User;

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
public class Discussion extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "problem_id", nullable = false)
	private Problem problem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "language_id", nullable = false)
	private Language language;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private boolean isDeleted;

	@Builder
	public Discussion(User user, Problem problem, Language language, String content) {
		this.user = user;
		this.problem = problem;
		this.language = language;
		this.content = content;
	}

	public void update(Language language, String content) {
		this.language = language;
		this.content = content;
	}

	public void setDeleted() {
		this.isDeleted = true;
	}

	public boolean isProblemMatches(Long problemId) {
		return Objects.equals(this.problem.getId(), problemId);
	}

	public boolean isAuthor(Long userId) {
		return Objects.equals(this.user.getId(), userId);
	}

	// Getter
	public String getUserEmail() {
		return this.user.getEmail();
	}

	public Long getProblemId() {
		return this.problem.getId();
	}

	public Long getLanguageId() {
		return this.language.getId();
	}
}
