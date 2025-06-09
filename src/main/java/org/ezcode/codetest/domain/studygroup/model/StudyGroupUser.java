package org.ezcode.codetest.domain.studygroup.model;

import static jakarta.persistence.FetchType.*;

import java.time.LocalDateTime;

import org.ezcode.codetest.domain.user.model.entity.User;
import org.hibernate.annotations.CreationTimestamp;

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

@Entity
@Table(name = "study_group_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGroupUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "study_group_id", nullable = false)
	private StudyGroup studyGroup;

	@Column(nullable = false)
	private Boolean isOwner;

	@CreationTimestamp
	@Column(name = "joined_at", nullable = false)
	private LocalDateTime joinedAt;

	@Builder
	public StudyGroupUser(User user, StudyGroup studyGroup, Boolean isOwner) {
		this.user = user;
		this.studyGroup = studyGroup;
		this.isOwner = isOwner;
	}
}
