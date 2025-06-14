package org.ezcode.codetest.infrastructure.mongodb.repository;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.entity.Item;
import org.ezcode.codetest.domain.game.model.enums.ItemCategory;
import org.ezcode.codetest.domain.game.repository.ItemRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

	private final ItemMongoRepository itemRepository;

	public Optional<Item> findById(String id) {

		return itemRepository.findById(id);
	}

	public Item save(Item item) {

		return itemRepository.save(item);
	}

	public void delete(Item item) {

		itemRepository.delete(item);
	}

	public List<Item> findAll() {

		return itemRepository.findAll();
	}

	public List<Item> findAllByItemCategory(ItemCategory category) {

		return itemRepository.findAllByItemCategory(category);
	}

	public Optional<Item> findByName(String name) {

		return itemRepository.findByName(name);
	}

	public List<Item> findByIdIn(List<String> ids) {

		return itemRepository.findByIdIn(ids);
	}

}
