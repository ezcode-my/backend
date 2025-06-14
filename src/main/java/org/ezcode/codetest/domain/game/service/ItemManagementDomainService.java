package org.ezcode.codetest.domain.game.service;

import java.util.List;

import org.ezcode.codetest.domain.game.model.entity.Item;
import org.ezcode.codetest.domain.game.repository.ItemRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemManagementDomainService {

	private final ItemRepository itemRepository;


	public Item createItem(Item item) {

		return itemRepository.save(item);
	}

	public void deleteItem(Item item) {

		itemRepository.delete(item);
	}

	public List<Item> getAllItemList() {

		return itemRepository.findAll();
	}

}
