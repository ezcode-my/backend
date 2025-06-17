package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.character;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface GameCharacterJpaRepository extends JpaRepository<GameCharacter, Long> {

	@EntityGraph(attributePaths = "stats")
	Optional<GameCharacter> findByUserId(Long userId);

	@Query("""
		  SELECT g
		  FROM GameCharacter g
		  WHERE g.user.id <> :userId
		  ORDER BY function('RAND')
		""")
	@EntityGraph(attributePaths = "stats")
	List<GameCharacter> findRandomCharacter(@Param("userId") Long userId, Pageable pageable);
}
