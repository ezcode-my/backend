package org.ezcode.codetest.domain.game.service;

import java.util.List;

import org.ezcode.codetest.domain.game.model.entity.CharacterRealStat;
import org.ezcode.codetest.domain.game.model.entity.GameCharacter;
import org.ezcode.codetest.domain.game.model.entity.GameCharacterSkill;
import org.ezcode.codetest.domain.game.model.vo.BattleLog;
import org.ezcode.codetest.domain.game.model.vo.CharacterContext;
import org.ezcode.codetest.domain.game.strategy.SkillStrategy;
import org.ezcode.codetest.domain.game.strategy.SkillStrategyFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameEncounterDomainService {

	private final CharacterEquipService characterEquipService;
	private final SkillStrategyFactory  skillStrategyFactory;

	public BattleLog battle(GameCharacter player, GameCharacter opponent) {

		List<GameCharacterSkill> playerEquippedSkills = characterEquipService.loadEquippedSkills(player);
		List<GameCharacterSkill> opponentEquippedSkills = characterEquipService.loadEquippedSkills(opponent);

		List<SkillStrategy> playerSkillStrategies = skillStrategyFactory.orderedStrategies(playerEquippedSkills);
		List<SkillStrategy> opponentSkillStrategies = skillStrategyFactory.orderedStrategies(opponentEquippedSkills);

		CharacterRealStat playerStats = new CharacterRealStat(player.getRealStat());
		CharacterRealStat opponentStats = new CharacterRealStat(opponent.getRealStat());
		playerStats.applyItemRealStat(characterEquipService.loadEquippedItems(player));
		opponentStats.applyItemRealStat(characterEquipService.loadEquippedItems(opponent));

		CharacterContext playerContext = CharacterContext.from(player.getName(), playerStats);
		CharacterContext opponentContext = CharacterContext.from(opponent.getName(), opponentStats);

		BattleLog battleLog = new BattleLog();
		int currentSkillIndex = 0;

		while (playerContext.checkActionPoints() || opponentContext.checkActionPoints()) {

			boolean isPlayerFirst = playerContext.checkSpeed(opponentContext.getSpeed());

			CharacterContext firstAttacker = isPlayerFirst ? playerContext : opponentContext;
			CharacterContext firstDefender = isPlayerFirst ? opponentContext : playerContext;

			List<SkillStrategy> firstAttackerStrategies = isPlayerFirst ? playerSkillStrategies : opponentSkillStrategies;
			List<SkillStrategy> firstDefenderStrategies = isPlayerFirst ? opponentSkillStrategies : playerSkillStrategies;

			boolean attackerAlive = true;
			if (firstAttacker.checkActionPoints()) {
				attackerAlive = firstAttackerStrategies.get(currentSkillIndex)
					.useSkill(firstAttacker, firstDefender, battleLog);
			}

			if (!attackerAlive) {
				battleLog.setPlayerWin(firstAttacker == playerContext);
				return battleLog;
			}

			boolean defenderAlive = true;
			if (firstDefender.checkActionPoints()) {
				defenderAlive = firstDefenderStrategies.get(currentSkillIndex)
					.useSkill(firstDefender, firstAttacker, battleLog);
			}

			if (!defenderAlive) {
				battleLog.setPlayerWin(firstDefender == playerContext);
				return battleLog;
			}

			currentSkillIndex = (currentSkillIndex + 1) % 3;
		}

		battleLog.add("전투가 종료되었습니다. 양쪽 모두 살아남았습니다. 무승부입니다!");
		battleLog.setPlayerWin(false);
		return battleLog;
	}

}
