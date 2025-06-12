package org.ezcode.codetest.infrastructure.persistence.repository.game;

import org.ezcode.codetest.domain.game.model.entity.Inventory;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class InventoryRepositoryImpl {

	private final InventoryJpaRepository inventoryRepository;


	public Inventory save(Inventory inventory) {

		return inventoryRepository.save(inventory);
	}

}
