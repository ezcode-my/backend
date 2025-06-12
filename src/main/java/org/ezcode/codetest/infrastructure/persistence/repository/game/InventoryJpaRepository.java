package org.ezcode.codetest.infrastructure.persistence.repository.game;

import org.ezcode.codetest.domain.game.model.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryJpaRepository extends JpaRepository<Inventory, Long> {
}
