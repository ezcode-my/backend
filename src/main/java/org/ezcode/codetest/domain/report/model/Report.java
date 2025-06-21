package org.ezcode.codetest.domain.report.model;

import jakarta.persistence.*;
import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.user.model.entity.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Report extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reporter_id", nullable = false)
	private User reporter;

	@Column(nullable = false)
	private Long targetId;

	@Column(nullable = false, length = 50)
	@Enumerated(EnumType.STRING)
	private ReportTargetType targetType;
	@Column(nullable = false)
	private String message;

	private String imageUrl;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReportType reportType;

	@Column(nullable = false, length = 50)
	@Enumerated(EnumType.STRING)
	private ReportStatus reportStatus;

	public Report(User reporter, Long targetId, ReportTargetType targetType,
				  String message, String imageUrl, ReportType reportType) {
		this.reporter = reporter;
		this.targetId = targetId;
		this.targetType = targetType;
		this.message = message;
		this.imageUrl = imageUrl;
		this.reportType = reportType;
		this.reportStatus = ReportStatus.PENDING;
	}

	public void updateStatus(ReportStatus newStatus) {
		this.reportStatus = newStatus;
	}

}
