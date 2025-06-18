package org.ezcode.codetest.application.game.management;

import org.ezcode.codetest.application.game.dto.request.encounter.EncounterChoiceDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.encounter.EncounterChoiceSaveRequest;
import org.ezcode.codetest.application.game.dto.request.item.ItemDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.item.ItemSaveRequest;
import org.ezcode.codetest.application.game.dto.request.encounter.RandomEncounterDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.encounter.RandomEncounterSaveRequest;
import org.ezcode.codetest.application.game.dto.request.skill.SkillDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.skill.SkillSaveRequest;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounter;
import org.ezcode.codetest.domain.game.service.GameManagementDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameAdminUseCase {

	private final GameManagementDomainService managementService;

	@Transactional
	public void createItem(ItemSaveRequest request) {

		managementService.createItem(request.toItem());
	}

	@Transactional
	public void deleteItem(ItemDeleteRequest request) {

		managementService.deleteItem(request.name());
	}

	@Transactional
	public void createRandomEncounter(RandomEncounterSaveRequest request) {

		managementService.createRandomEncounter(request.toRandomEncounter());
	}

	@Transactional
	public void deleteRandomEncounter(RandomEncounterDeleteRequest request) {

		managementService.deleteRandomEncounter(request.name());
	}

	@Transactional
	public void createEncounterChoice(EncounterChoiceSaveRequest request) {

		RandomEncounter encounter = managementService.getRandomEncounter(request.encounterName());

		managementService.createEncounterChoice(request.toEncounterChoice(encounter));
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
}
