package org.ezcode.codetest.infrastructure.persistence.repository.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ezcode.codetest.domain.report.model.Report;

public interface ReportJpaRepository extends JpaRepository<Report, Long> {
}
