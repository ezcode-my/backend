package org.ezcode.codetest.domain.report.repository;

import org.ezcode.codetest.domain.report.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
