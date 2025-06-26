package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.character;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.character.CategoryStat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryStatJpaRepository extends JpaRepository<CategoryStat, Long> {

	Optional<CategoryStat> findByCategoryId(Long categoryId);

	@EntityGraph(attributePaths = "stats")
	Optional<CategoryStat> findByCategoryKorName(String categoryKorName);
}
