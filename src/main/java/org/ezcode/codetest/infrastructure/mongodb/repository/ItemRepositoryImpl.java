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

	@Override
	public Optional<Item> findById(String id) {

		return itemRepository.findById(id);
	}

	@Override
	public boolean existsByName(String name) {

		return itemRepository.existsByName(name);
	}

	@Override
	public Long count() {

		return itemRepository.count();
	}

	@Override
	public Item save(Item item) {

		return itemRepository.save(item);
	}

	@Override
	public List<Item> saveAll(List<Item> items) {

		return itemRepository.saveAll(items);
	}

	@Override
	public void delete(Item item) {

		itemRepository.delete(item);
	}

	@Override
	public void deleteByName(String name) {

		itemRepository.deleteByName(name);
	}

	@Override
	public List<Item> findAll() {

		return itemRepository.findAll();
	}

	@Override
	public List<Item> findAllByItemCategory(ItemCategory category) {

		return itemRepository.findAllByItemCategory(category);
	}

	@Override
	public Optional<Item> findByName(String name) {

		return itemRepository.findByName(name);
	}

	@Override
	public List<Item> findByIdIn(List<String> ids) {

		return itemRepository.findByIdIn(ids);
	}

}
