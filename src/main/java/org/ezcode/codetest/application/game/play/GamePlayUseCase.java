package org.ezcode.codetest.application.game.play;

import java.util.List;

import org.ezcode.codetest.application.game.dto.mapper.GameMapper;
import org.ezcode.codetest.application.game.dto.request.encounter.BattleRequest;
import org.ezcode.codetest.application.game.dto.request.encounter.EncounterChoiceRequest;
import org.ezcode.codetest.application.game.dto.request.skill.SkillEquipRequest;
import org.ezcode.codetest.application.game.dto.request.skill.SkillUnEquipRequest;
import org.ezcode.codetest.application.game.dto.response.character.CharacterStatusResponse;
import org.ezcode.codetest.application.game.dto.response.encounter.BattleHistoryResponse;
import org.ezcode.codetest.application.game.dto.response.encounter.DefenceBattleHistoryResponse;
import org.ezcode.codetest.application.game.dto.response.encounter.EncounterResultResponse;
import org.ezcode.codetest.application.game.dto.response.encounter.MatchingBattleResponse;
import org.ezcode.codetest.application.game.dto.response.encounter.MatchingEncounterResponse;
import org.ezcode.codetest.application.game.dto.response.item.ItemGamblingResponse;
import org.ezcode.codetest.application.game.dto.response.item.ItemResponse;
import org.ezcode.codetest.application.game.dto.response.skill.SkillGamblingResponse;
import org.ezcode.codetest.application.game.dto.response.skill.SkillResponse;
import org.ezcode.codetest.common.security.util.JwtUtil;
import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.encounter.BattleHistory;
import org.ezcode.codetest.domain.game.model.encounter.EncounterHistory;
import org.ezcode.codetest.domain.game.model.encounter.EncounterLog;
import org.ezcode.codetest.domain.game.model.encounter.MatchMessageTemplate;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounter;
import org.ezcode.codetest.domain.game.model.skill.GameCharacterSkill;
import org.ezcode.codetest.domain.game.model.item.Item;
import org.ezcode.codetest.domain.game.model.skill.Skill;
import org.ezcode.codetest.domain.game.model.item.ItemCategory;
import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.service.CharacterStatusDomainService;
import org.ezcode.codetest.domain.game.service.GameEncounterDomainService;
import org.ezcode.codetest.domain.game.service.GameShopDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GamePlayUseCase {

	private final CharacterStatusDomainService characterService;
	private final GameShopDomainService gameShopService;
	private final UserDomainService userDomainService;
	private final GameEncounterDomainService encounterDomainService;
	private final GameMapper gameMapper;
	private final JwtUtil jwtUtil;

	@Transactional
	public void createCharacter(String email) {

		User user = userDomainService.getUser(email);

		characterService.createGameCharacter(new GameCharacter(user));
	}

	@Transactional
	public CharacterStatusResponse characterStatusOpen(Long userId) {

		GameCharacter character = characterService.getGameCharacter(userId);

		List<Item> equippedItems = characterService.loadEquippedItems(character);

		List<GameCharacterSkill> equippedSkills = characterService.loadEquippedSkills(character);

		return gameMapper.toCharacterStatusResponse(character, equippedItems, equippedSkills);
	}

	@Transactional
	public ItemGamblingResponse gamblingForItem(Long userId, String itemCategory) {

		GameCharacter character = characterService.getGameCharacter(userId);

		Item newItem = switch (ItemCategory.valueOf(itemCategory.toUpperCase())) {
			case WEAPON -> gameShopService.gamblingNewWeapon(character);
			case DEFENCE -> gameShopService.gamblingNewDefence(character);
			case ACCESSORY -> gameShopService.gamblingNewAccessory(character);
		};

		return ItemGamblingResponse.from(gameMapper.toItemResponse(newItem));
	}

	@Transactional
	public List<ItemResponse> inventoryOpen(Long userId) {

		GameCharacter character = characterService.getGameCharacter(userId);

		List<Item> inventoryItems = characterService.inventoryOpen(character.getId());

		return inventoryItems.stream().map(gameMapper::toItemResponse).toList();
	}

	@Transactional
	public List<SkillResponse> skillInventoryOpen(Long userId) {

		GameCharacter character = characterService.getGameCharacter(userId);

		List<GameCharacterSkill> characterSkills = characterService.loadUnEquippedSkills(character);

		return characterSkills.stream().map(cs -> SkillResponse.from(cs.getSkill())).toList();
	}

	@Transactional
	public void equipItem(Long userId, String itemName) {

		GameCharacter character = characterService.getGameCharacter(userId);

		characterService.equipItem(character, itemName);
	}

	@Transactional
	public void equipSkill(Long userId, SkillEquipRequest request) {

		GameCharacter character = characterService.getGameCharacter(userId);

		characterService.equipSkill(character, request.name(), request.slotNumber());
	}

	@Transactional
	public void unEquipSkill(Long userId, SkillUnEquipRequest request) {

		GameCharacter character = characterService.getGameCharacter(userId);

		characterService.unEquipSkill(character, request.name());
	}

	@Transactional
	public SkillGamblingResponse gamblingForSkill(Long userId) {

		GameCharacter character = characterService.getGameCharacter(userId);

		Skill skill = gameShopService.gamblingNewSkill(character);

		return SkillGamblingResponse.from(SkillResponse.from(skill));
	}

	@Transactional
	public BattleHistoryResponse battle(Long playerId, BattleRequest request) {

		Claims claims = jwtUtil.extractClaims(request.battleToken());

		Long enemyId = Long.valueOf(claims.getSubject());

		GameCharacter playerCharacter = characterService.getGameCharacter(playerId);

		GameCharacter enemyCharacter = characterService.getGameCharacter(enemyId);

		BattleLog log = encounterDomainService.battle(playerCharacter, enemyCharacter);

		encounterDomainService.createBattleHistory(playerCharacter, enemyCharacter, log);

		return BattleHistoryResponse.of(
			playerCharacter.getName(),
			enemyCharacter.getName(),
			log.getMessages(),
			log.getPlayerWin()
		);
	}

	@Transactional
	public MatchingBattleResponse randomBattleMatching(Long userId) {

		GameCharacter player = characterService.getGameCharacter(userId);

		GameCharacter enemy = encounterDomainService.getRandomEnemyCharacter(userId, player.getId());

		boolean checkStrength = encounterDomainService.compareStrength(player, enemy);

		String matchMessage = MatchMessageTemplate.random(player.getName(), enemy.getName());

		Long enemyUserId = enemy.getUser().getId();

		return MatchingBattleResponse.of(checkStrength, matchMessage, jwtUtil.createGameToken(enemyUserId));
	}

	@Transactional
	public MatchingEncounterResponse randomEncounterMatching(Long userId) {

		GameCharacter playerCharacter = characterService.getGameCharacter(userId);

		RandomEncounter encounter = encounterDomainService.getRandomEncounter(playerCharacter.getId());

		return MatchingEncounterResponse.from(encounter, jwtUtil.createGameToken(encounter.getId()));
	}

	@Transactional
	public EncounterResultResponse encounterChoice(Long userId, EncounterChoiceRequest request) {

		Claims claims = jwtUtil.extractClaims(request.encounterToken());

		Long encounterId = Long.valueOf(claims.getSubject());

		GameCharacter player = characterService.getGameCharacter(userId);

		EncounterLog resultLog = encounterDomainService.encounterHappen(player, encounterId,
			request.playerDecision());

		EncounterHistory history = encounterDomainService.createEncounterHistory(player, resultLog);

		return EncounterResultResponse.from(history);
	}

	@Transactional
	public List<DefenceBattleHistoryResponse> getPlayerDefenceHistory(Long userId) {

		GameCharacter playerCharacter = characterService.getGameCharacter(userId);

		List<BattleHistory> history = encounterDomainService.getCharacterBattleHistory(playerCharacter);

		return history.stream().map(DefenceBattleHistoryResponse::from).toList();
	}

}
