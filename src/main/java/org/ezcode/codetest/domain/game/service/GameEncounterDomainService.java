package org.ezcode.codetest.domain.game.service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.exception.GameExceptionCode;
import org.ezcode.codetest.domain.game.model.character.CharacterRealStat;
import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.character.Inventory;
import org.ezcode.codetest.domain.game.model.encounter.BattleHistory;
import org.ezcode.codetest.domain.game.model.encounter.EncounterChoice;
import org.ezcode.codetest.domain.game.model.encounter.EncounterHistory;
import org.ezcode.codetest.domain.game.model.encounter.EncounterLog;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounter;
import org.ezcode.codetest.domain.game.model.item.Item;
import org.ezcode.codetest.domain.game.model.item.Weapon;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.GameCharacterSkill;
import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.repository.BattleHistoryRepository;
import org.ezcode.codetest.domain.game.repository.EncounterChoiceRepository;
import org.ezcode.codetest.domain.game.repository.EncounterHistoryRepository;
import org.ezcode.codetest.domain.game.repository.GameCharacterRepository;
import org.ezcode.codetest.domain.game.repository.InventoryRepository;
import org.ezcode.codetest.domain.game.repository.RandomEncounterRepository;
import org.ezcode.codetest.domain.game.strategy.encounter.EncounterStrategy;
import org.ezcode.codetest.domain.game.strategy.encounter.EncounterStrategyFactory;
import org.ezcode.codetest.domain.game.strategy.skill.SkillStrategy;
import org.ezcode.codetest.domain.game.strategy.skill.SkillStrategyFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameEncounterDomainService {

	private final CharacterEquipService characterEquipService;
	private final SkillStrategyFactory  skillStrategyFactory;
	private final EncounterStrategyFactory encounterFactory;

	private final BattleHistoryRepository battleHistoryRepository;
	private final EncounterHistoryRepository encounterHistoryRepository;

	private final GameCharacterRepository characterRepository;
	private final InventoryRepository inventoryRepository;
	private final RandomEncounterRepository encounterRepository;
	private final EncounterChoiceRepository choiceRepository;

	public BattleLog battle(GameCharacter player, GameCharacter opponent) {

		List<GameCharacterSkill> playerEquippedSkills = characterEquipService.loadEquippedSkills(player);
		List<GameCharacterSkill> opponentEquippedSkills = characterEquipService.loadEquippedSkills(opponent);

		List<SkillStrategy> playerSkillStrategies = skillStrategyFactory.orderedStrategies(playerEquippedSkills);
		List<SkillStrategy> opponentSkillStrategies = skillStrategyFactory.orderedStrategies(opponentEquippedSkills);

		List<Item> playerItems = characterEquipService.loadEquippedItems(player);
		List<Item> opponentItems = characterEquipService.loadEquippedItems(opponent);

		WeaponType playerWeaponType = playerItems.stream()
			.filter(item -> item instanceof Weapon)
			.map(item -> (WeaponType) item.getItemType())
			.findFirst()
			.orElse(WeaponType.NOTHING);

		WeaponType opponentWeaponType = opponentItems.stream()
			.filter(item -> item instanceof Weapon)
			.map(item -> (WeaponType) item.getItemType())
			.findFirst()
			.orElse(WeaponType.NOTHING);

		CharacterRealStat playerStats = new CharacterRealStat(player.getRealStat());
		CharacterRealStat opponentStats = new CharacterRealStat(opponent.getRealStat());
		playerStats.applyItemRealStat(playerItems);
		opponentStats.applyItemRealStat(opponentItems);

		CharacterContext playerContext = CharacterContext.from(player.getName(), playerStats);
		CharacterContext opponentContext = CharacterContext.from(opponent.getName(), opponentStats);

		BattleLog battleLog = new BattleLog();
		int currentSkillIndex = 0;

		while (playerContext.checkActionPoints() || opponentContext.checkActionPoints()) {
			boolean isPlayerFirst = playerContext.checkSpeed(opponentContext.getSpeed());

			CharacterContext attacker = isPlayerFirst ? playerContext : opponentContext;
			CharacterContext defender = isPlayerFirst ? opponentContext : playerContext;

			List<SkillStrategy> attackerStrategies = isPlayerFirst ? playerSkillStrategies : opponentSkillStrategies;
			WeaponType attackerWeapon = isPlayerFirst ? playerWeaponType : opponentWeaponType;

			boolean alive = true;
			if (attacker.checkActionPoints()) {
				alive = attackerStrategies.get(currentSkillIndex)
					.useSkill(attacker, defender, battleLog, attackerWeapon);
			}

			if (!alive) {
				battleLog.setPlayerWin(attacker == playerContext);
				return battleLog;
			}

			SkillStrategy defenderStrategy = isPlayerFirst ? opponentSkillStrategies.get(currentSkillIndex)
				: playerSkillStrategies.get(currentSkillIndex);
			WeaponType defenderWeapon = isPlayerFirst ? opponentWeaponType : playerWeaponType;

			if (defender.checkActionPoints()) {
				alive = defenderStrategy.useSkill(defender, attacker, battleLog, defenderWeapon);
			}

			if (!alive) {
				battleLog.setPlayerWin(defender == playerContext);
				return battleLog;
			}

			currentSkillIndex = (currentSkillIndex + 1) % 3;
		}

		battleLog.add("전투가 종료되었습니다. 양쪽 모두 살아남았습니다. 무승부입니다!");
		battleLog.setPlayerWin(false);
		return battleLog;
	}

	public BattleHistory createBattleHistory(GameCharacter player, GameCharacter opponent, BattleLog log) {

		return battleHistoryRepository.save(BattleHistory.builder()
			.attacker(player)
			.defender(opponent)
			.battleLog(log.getMessages())
			.isAttackerWin(log.getPlayerWin())
			.build());
	}

	public boolean compareStrength(GameCharacter player, GameCharacter opponent) {

		CharacterRealStat playerStats = new CharacterRealStat(player.getRealStat());

		CharacterRealStat opponentStats = new CharacterRealStat(opponent.getRealStat());

		playerStats.applyItemRealStat(characterEquipService.loadEquippedItems(player));

		opponentStats.applyItemRealStat(characterEquipService.loadEquippedItems(opponent));

		return playerStats.statSummary() < opponentStats.statSummary();
	}

	public List<BattleHistory> getBattleHistory(GameCharacter character) {

		return battleHistoryRepository.findByCharacterId(character.getId());
	}

	public GameCharacter getRandomEnemyCharacter(Long userId) {

		return characterRepository.findRandomCharacter(userId)
			.orElseThrow(() -> new GameException(GameExceptionCode.RANDOM_CHARACTER_MATCHING_FAIL));
	}

	public RandomEncounter getRandomEncounter() {

		List<RandomEncounter> encounters = encounterRepository.findAllEncounters();

		int randomIndex = ThreadLocalRandom.current().nextInt(encounters.size());

		return encounters.get(randomIndex);
	}

	public EncounterLog encounterHappen(GameCharacter player, Long encounterId, boolean playerDecision) {

		List<EncounterChoice> choices = choiceRepository.findChoiceByPlayerDecision(encounterId, playerDecision);

		int randomIndex = ThreadLocalRandom.current().nextInt(choices.size());

		EncounterChoice choice = choices.get(randomIndex);

		EncounterStrategy strategy = encounterFactory.getStrategy(choice.getEncounterEffect());

		List<Item> playerItems = characterEquipService.loadEquippedItems(player);

		CharacterRealStat playerStats = new CharacterRealStat(player.getRealStat());

		playerStats.applyItemRealStat(playerItems);

		CharacterContext playerContext = CharacterContext.from(player.getName(), playerStats);

		Inventory playerInventory = inventoryRepository.findByGameCharacterId(player.getId())
				.orElseThrow(() -> new GameException(GameExceptionCode.INVENTORY_NOT_FOUND));

		EncounterLog resultLog = new EncounterLog();

		strategy.eventHappen(player, playerInventory, playerContext, resultLog);

		return resultLog;
	}

	public EncounterHistory createEncounterHistory(GameCharacter player, EncounterLog log) {

		return encounterHistoryRepository.save(EncounterHistory.builder()
			.character(player)
			.resultLog(log.getMessages())
			.isPositive(log.getIsPositive())
			.build());
	}

}
