package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import org.ezcode.codetest.domain.problem.model.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionJpaRepository extends JpaRepository<Submission, Long> {
}
