package org.ezcode.codetest.domain.game.repository;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.character.Inventory;

public interface InventoryRepository {

	Inventory save(Inventory inventory);

	Optional<Inventory> findByGameCharacterId(Long gameCharacterId);



}
