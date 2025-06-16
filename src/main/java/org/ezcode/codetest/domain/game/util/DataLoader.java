package org.ezcode.codetest.domain.game.util;

import java.util.List;

import org.ezcode.codetest.domain.game.model.entity.Accessory;
import org.ezcode.codetest.domain.game.model.entity.Defence;
import org.ezcode.codetest.domain.game.model.entity.Weapon;
import org.ezcode.codetest.domain.game.model.enums.AccessoryType;
import org.ezcode.codetest.domain.game.model.enums.DefenceType;
import org.ezcode.codetest.domain.game.model.enums.Grade;
import org.ezcode.codetest.domain.game.model.enums.WeaponType;
import org.ezcode.codetest.domain.game.repository.ItemRepository;
import org.ezcode.codetest.domain.game.repository.SkillRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader {

	private final ItemRepository itemRepository;
	private final SkillRepository skillRepository;

	@PostConstruct
	@Transactional
	public void init() {

		if (itemRepository.count() == 0L) {
			itemRepository.saveAll(List.of(
				Weapon.builder()
					.grade(Grade.TRASH)
					.name("기본 무기")
					.atk(25)
					.speed(1)
					.accuracy(1)
					.crit(0)
					.description("기본 단검입니다. 꽝이에용")
					.stun(0)
					.type(WeaponType.SHORT_SWORD)
					.build() ,
				Defence.builder()
					.grade(Grade.TRASH)
				.name("기본 방어구")
				.def(1)
				.speed(0)
					.evasion(1)
				.description("기본 갑빠입니다. 꽝이에용")
				.type(DefenceType.ARMOR)
				.build(),
				Accessory.builder()
					.grade(Grade.TRASH)
					.name("기본 악세서리")
					.speed(1)
					.accuracy(1)
					.evasion(0)
					.crit(0)
					.description("기본 악세서리입니다. 꽝이에용")
					.stun(0)
					.type(AccessoryType.MOUSE)
					.build()
			));
		}
	}
}
