package org.ezcode.codetest.domain.game.service;

import java.util.List;

import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.exception.GameExceptionCode;
import org.ezcode.codetest.domain.game.model.entity.EncounterChoice;
import org.ezcode.codetest.domain.game.model.entity.Item;
import org.ezcode.codetest.domain.game.model.entity.RandomEncounter;
import org.ezcode.codetest.domain.game.model.entity.Skill;
import org.ezcode.codetest.domain.game.repository.EncounterChoiceRepository;
import org.ezcode.codetest.domain.game.repository.ItemRepository;
import org.ezcode.codetest.domain.game.repository.RandomEncounterRepository;
import org.ezcode.codetest.domain.game.repository.SkillRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameManagementDomainService {

	private final ItemRepository itemRepository;
	private final SkillRepository skillRepository;
	private final RandomEncounterRepository encounterRepository;
	private final EncounterChoiceRepository choiceRepository;

	public Item createItem(Item item) {

		boolean exists = itemRepository.existsByName(item.getName());

		if(exists) throw new GameException(GameExceptionCode.ITEM_ALREADY_EXISTS);

		return itemRepository.save(item);
	}

	public void deleteItem(String name) {

		boolean exists = itemRepository.existsByName(name);

		if(!exists) throw new GameException(GameExceptionCode.ITEM_NOT_EXISTS);

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

		if(!exists) throw new GameException(GameExceptionCode.SKILL_NOT_EXISTS);

		skillRepository.deleteByName(name);
	}

	public List<Skill> getAllSkillList() {

		return skillRepository.findAll();
	}

	public RandomEncounter createRandomEncounter(RandomEncounter encounter) {

		boolean exists = encounterRepository.existsByName(encounter.getName());

		if(exists) throw new GameException(GameExceptionCode.RANDOM_ENCOUNTER_ALREADY_EXISTS);

		return encounterRepository.save(encounter);
	}

	public RandomEncounter findRandomEncounter(String name) {

		return encounterRepository.findByName(name)
			.orElseThrow(() -> new GameException(GameExceptionCode.RANDOM_ENCOUNTER_NOT_EXISTS));
	}

	public void deleteRandomEncounter(String name) {

		RandomEncounter encounter = encounterRepository.findByName(name)
			.orElseThrow(() -> new GameException(GameExceptionCode.RANDOM_ENCOUNTER_NOT_EXISTS));

		encounter.softDelete();
	}

	public List<RandomEncounter> getAllRandomEncounterList() {

		return encounterRepository.findAll();
	}

	public EncounterChoice createEncounterChoice(EncounterChoice choice) {

		boolean exists = choiceRepository.existsByName(choice.getName());

		if(exists) throw new GameException(GameExceptionCode.ENCOUNTER_CHOICE_ALREADY_EXISTS);

		return choiceRepository.save(choice);
	}

	public void deleteEncounterChoice(String name) {

		boolean exists = choiceRepository.existsByName(name);

		if(!exists) throw new GameException(GameExceptionCode.ENCOUNTER_CHOICE_NOT_EXISTS);

		choiceRepository.deleteByName(name);
	}

	public List<EncounterChoice> getAllEncounterChoiceList() {

		return choiceRepository.findAll();
	}

}
