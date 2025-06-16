package org.ezcode.codetest.domain.game.model.vo;

import java.util.List;

import org.ezcode.codetest.domain.game.strategy.SkillStrategy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterContext {

	private Double atk;
	private Double def;
	private Double speed;
	private Double crit;
	private Double stun;
	private Double evasion;
	private Double accuracy;
	private Double hp;
	private Integer ap;

	private List<SkillStrategy> skillList;

 }



