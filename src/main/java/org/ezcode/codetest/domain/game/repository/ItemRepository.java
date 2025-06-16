package org.ezcode.codetest.domain.game.repository;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.entity.Item;
import org.ezcode.codetest.domain.game.model.enums.ItemCategory;

public interface ItemRepository {

	Item save(Item item);

	void saveAll(List<Item> items);

	boolean existsByName(String name);

	void delete(Item item);

	void deleteByName(String name);

	Long count();

	List<Item> findAll();

	List<Item> findAllByItemCategory(ItemCategory category);

	Optional<Item> findByName(String itemName);

	List<Item> findByIdIn(List<String> ids);

	Optional<Item> findById(String id);
}
