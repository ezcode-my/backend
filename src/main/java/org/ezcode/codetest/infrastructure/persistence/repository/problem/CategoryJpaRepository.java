package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.problem.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByCode(String categoryCode);

	List<Category> findAllByCodeIn(List<String> categoryCodes);
}
