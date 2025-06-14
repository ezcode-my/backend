package org.ezcode.codetest.domain.game.service;

import java.util.List;

import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.exception.GameExceptionCode;
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

		boolean exists = itemRepository.existsByName(item.getName());

		if(exists) throw new GameException(GameExceptionCode.ITEM_ALREADY_EXISTS);

		return itemRepository.save(item);
	}

	public void deleteItem(String name) {

		boolean exists = itemRepository.existsByName(name);

		if(!exists) throw new GameException(GameExceptionCode.ITEM_NOT_FOUND);

		itemRepository.deleteByName(name);
	}

	public List<Item> getAllItemList() {

		return itemRepository.findAll();
	}

	public Skill createSkill(Skill skill) {

		boolean exists = skillRepository.existsByName(skill.getName());

		if(exists) throw new GameException(GameExceptionCode.SKILL_ALREADY_EXISTS);

		return skillRepository.save(skill);
	}

	public void deleteSkill(String name) {

		boolean exists = skillRepository.existsByName(name);

		if(!exists) throw new GameException(GameExceptionCode.SKILL_NOT_FOUND);

		skillRepository.deleteByName(name);
	}

}
