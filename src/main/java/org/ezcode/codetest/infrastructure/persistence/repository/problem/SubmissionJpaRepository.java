package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionJpaRepository extends JpaRepository<Submission, Long> {
	List<Submission> findAllByUser_Id(Long userId);
}
