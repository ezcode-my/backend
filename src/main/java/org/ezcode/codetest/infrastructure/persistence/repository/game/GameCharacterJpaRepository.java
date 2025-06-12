package org.ezcode.codetest.infrastructure.persistence.repository.game;

import org.ezcode.codetest.domain.game.model.entity.GameCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameCharacterJpaRepository extends JpaRepository<GameCharacter, Long> {
}
