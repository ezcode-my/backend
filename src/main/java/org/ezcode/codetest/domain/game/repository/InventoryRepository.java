package org.ezcode.codetest.domain.game.repository;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.entity.Inventory;

public interface InventoryRepository {

	Inventory save(Inventory inventory);

	Optional<Inventory> findByGameCharacterId(Long gameCharacterId);



}
