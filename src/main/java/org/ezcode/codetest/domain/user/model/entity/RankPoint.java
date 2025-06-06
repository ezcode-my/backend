package org.ezcode.codetest.domain.user.model.entity;

import java.time.LocalDateTime;

import org.ezcode.codetest.domain.submission.model.entity.UserProblemResult;
import org.springframework.data.annotation.CreatedDate;

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

@Getter
@Entity
@Table(name= "rank_point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RankPoint {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_problem_result_id", nullable = false)
	private UserProblemResult userProblemResult;

	@Column(nullable = false)
	private Integer point;

	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	protected LocalDateTime createdAt;

	@Builder
	public RankPoint(UserProblemResult userProblemResult, Integer point) {
		this.userProblemResult = userProblemResult;
		this.point = point;
		this.createdAt = LocalDateTime.now();
	}

}
