package org.ezcode.codetest.domain.report.model;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.user.model.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.ManyToOne;

@Getter
@Entity
@Table(name = "report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "reporter_id", nullable = false)
	private User reporter;

	@ManyToOne
	@JoinColumn(name = "target_id", nullable = false)
	private User target;

	@Column(nullable = false)
	private String message;

	@Column(nullable = false)
	private String imageUrl;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReportType reportType;

	@Enumerated(EnumType.STRING)
	private ReportStatus reportStatus;

	public Report(User reporter, User target, String message, String imageUrl,
		ReportType reportType) {
		this.reporter = reporter;
		this.target = target;
		this.message = message;
		this.imageUrl = imageUrl;
		this.reportType = reportType;
		this.reportStatus = ReportStatus.PENDING;
	}
}
