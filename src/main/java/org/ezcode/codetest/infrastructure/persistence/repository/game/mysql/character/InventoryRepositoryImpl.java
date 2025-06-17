package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.character;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.Character.Inventory;
import org.ezcode.codetest.domain.game.repository.InventoryRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class InventoryRepositoryImpl implements InventoryRepository {

	private final InventoryJpaRepository inventoryRepository;

	@Override
	public Inventory save(Inventory inventory) {

		return inventoryRepository.save(inventory);
	}

	@Override
	public Optional<Inventory> findByGameCharacterId(Long gameCharacterId) {

		return inventoryRepository.findByGameCharacterId(gameCharacterId);
	}
}
