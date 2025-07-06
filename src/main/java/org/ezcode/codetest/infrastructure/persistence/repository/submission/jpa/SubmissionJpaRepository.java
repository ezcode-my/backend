package org.ezcode.codetest.infrastructure.persistence.repository.submission.jpa;

import java.util.Collection;
import java.util.List;

import org.ezcode.codetest.domain.submission.model.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubmissionJpaRepository extends JpaRepository<Submission, Long> {
    List<Submission> findAllByUser_Id(Long userId);

    @Query("""
    SELECT COUNT(DISTINCT s.problem.id)
    FROM Submission s
    WHERE s.message = 'Accepted' AND s.user.id = :userId
""")
    Integer findAllByUserIdAndStatus(Long userId);
}
