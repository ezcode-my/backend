package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProblemSearchJpaRepository extends JpaRepository<Problem, Long> {

	// TODO: FULLTEXT 인덱스 추가
	@Query(value = "SELECT * FROM problem WHERE title LIKE CONCAT('%', :keyword, '%') OR description LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
	List<Problem> searchByKeyword(@Param("keyword") String keyword);
}
