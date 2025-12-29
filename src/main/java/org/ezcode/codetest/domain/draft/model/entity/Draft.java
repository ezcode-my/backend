package org.ezcode.codetest.domain.draft.model.entity;

import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.user.model.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "draft",
	uniqueConstraints = {
		// 언어별 저장 지원
		@UniqueConstraint(
			name = "uk_draft_user_problem_language",
			columnNames = {"user_id", "problem_id", "language_id"}
		)
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Draft {

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

	@Lob
	private String code;

	@Version        // 낙관적 락을 위한 버전 관리
	private Long version;

	public void updateCode(String newCode) {
		this.code = newCode;
	}

	@Builder
	public Draft(User user, Problem problem, Language language, String code, Long version) {
		this.user = user;
		this.problem = problem;
		this.language = language;
		this.code = code;
		this.version = version;
	}
}
