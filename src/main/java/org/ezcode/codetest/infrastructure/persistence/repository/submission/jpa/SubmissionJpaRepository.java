package org.ezcode.codetest.infrastructure.persistence.repository.submission.jpa;

import java.util.List;

import org.ezcode.codetest.domain.submission.model.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionJpaRepository extends JpaRepository<Submission, Long> {
    List<Submission> findAllByUser_Id(Long userId);
}
