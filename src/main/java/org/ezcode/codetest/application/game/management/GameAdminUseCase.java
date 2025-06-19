package org.ezcode.codetest.application.game.management;

import java.util.List;

import org.ezcode.codetest.application.game.dto.mapper.GameMapper;
import org.ezcode.codetest.application.game.dto.request.encounter.EncounterChoiceDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.encounter.EncounterChoiceSaveRequest;
import org.ezcode.codetest.application.game.dto.request.item.ItemDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.item.ItemSaveRequest;
import org.ezcode.codetest.application.game.dto.request.encounter.RandomEncounterDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.encounter.RandomEncounterSaveRequest;
import org.ezcode.codetest.application.game.dto.request.skill.SkillDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.skill.SkillSaveRequest;
import org.ezcode.codetest.application.game.dto.response.encounter.EncounterChoiceResponse;
import org.ezcode.codetest.application.game.dto.response.encounter.EncounterResponse;
import org.ezcode.codetest.application.game.dto.response.item.ItemResponse;
import org.ezcode.codetest.application.game.dto.response.skill.SkillResponse;
import org.ezcode.codetest.domain.game.model.encounter.EncounterChoice;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounter;
import org.ezcode.codetest.domain.game.model.item.Item;
import org.ezcode.codetest.domain.game.model.skill.Skill;
import org.ezcode.codetest.domain.game.service.GameManagementDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameAdminUseCase {

	private final GameManagementDomainService managementService;
	private final GameMapper gameMapper;

	@Transactional
	public void createItem(ItemSaveRequest request) {

		managementService.createItem(request.toItem());
	}

	@Transactional
	public void deleteItem(ItemDeleteRequest request) {

		managementService.deleteItem(request.name());
	}

	@Transactional(readOnly = true)
	public List<ItemResponse> getAllItems() {

		List<Item> items = managementService.getAllItemList();

		return items.stream().map(gameMapper::toItemResponse).toList();
	}

	@Transactional
	public void createRandomEncounter(RandomEncounterSaveRequest request) {

		managementService.createRandomEncounter(request.toRandomEncounter());
	}

	@Transactional
	public void deleteRandomEncounter(RandomEncounterDeleteRequest request) {

		managementService.deleteRandomEncounter(request.name());
	}

	@Transactional(readOnly = true)
	public List<EncounterResponse> getAllRandomEncounters() {

		List<RandomEncounter> encounters = managementService.getAllRandomEncounterList();

		return encounters.stream().map(gameMapper::toEncounterResponse).toList();
	}

	@Transactional
	public void createEncounterChoice(EncounterChoiceSaveRequest request) {

		RandomEncounter encounter = managementService.getRandomEncounter(request.encounterName());

		managementService.createEncounterChoice(request.toEncounterChoice(encounter));
	}

	@Transactional(readOnly = true)
	public List<EncounterChoiceResponse> getAllEncounterChoices() {

		List<EncounterChoice> choices = managementService.getAllEncounterChoiceList();

		return choices.stream().map(gameMapper::toEncounterChoiceResponse).toList();
	}


	@Transactional
	public void deleteEncounterChoice(EncounterChoiceDeleteRequest request) {

		managementService.deleteRandomEncounter(request.name());
	}

	@Transactional
	public void createSkill(SkillSaveRequest request) {

		managementService.createSkill(request.toSkill());
	}

	@Transactional
	public void deleteSkill(SkillDeleteRequest request) {

		managementService.deleteSkill(request.name());
	}

	@Transactional(readOnly = true)
	public List<SkillResponse> getAllSkills() {

		List<Skill> skills = managementService.getAllSkillList();

		return skills.stream().map(SkillResponse::from).toList();
	}

}
