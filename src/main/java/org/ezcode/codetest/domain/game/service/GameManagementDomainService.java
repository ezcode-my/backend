package org.ezcode.codetest.domain.game.service;

import java.util.List;

import org.ezcode.codetest.domain.game.model.entity.Item;
import org.ezcode.codetest.domain.game.model.entity.Skill;
import org.ezcode.codetest.domain.game.repository.ItemRepository;
import org.ezcode.codetest.domain.game.repository.SkillRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameManagementDomainService {

	private final ItemRepository itemRepository;
	private final SkillRepository skillRepository;


	public Item createItem(Item item) {

		return itemRepository.save(item);
	}

	public void deleteItem(String name) {

		itemRepository.deleteByName(name);
	}

	public List<Item> getAllItemList() {

		return itemRepository.findAll();
	}

	public Skill createSkill(Skill skill) {

		return skillRepository.save(skill);
	}

	public void deleteSkill(String name) {

		skillRepository.deleteByName(name);
	}

}
