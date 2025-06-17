package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.character;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.Character.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryJpaRepository extends JpaRepository<Inventory, Long> {

	Optional<Inventory> findByGameCharacterId(Long gameCharacterId);
}
