package org.ezcode.codetest.domain.user.model.entity;

import java.time.LocalDateTime;

import org.ezcode.codetest.domain.problem.model.entity.UserProblemStatus;
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
	@JoinColumn(name = "user_problem_status_id", nullable = false)
	private UserProblemStatus userProblemStatus;

	@Column(nullable = false)
	private Integer point;

	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	protected LocalDateTime createdAt;

	@Builder
	public RankPoint(UserProblemStatus userProblemStatus, Integer point) {
		this.userProblemStatus = userProblemStatus;
		this.point = point;
		this.createdAt = LocalDateTime.now();
	}

}
