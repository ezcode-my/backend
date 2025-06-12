package org.ezcode.codetest.domain.game.model.enums;

import java.util.concurrent.ThreadLocalRandom;

public interface Item {

	Integer getAtk();
	Integer getDef();
	Integer getCrit();
	Integer getStun();
	Integer getSpeed();
	Integer getEvasion();
	Integer getAccuracy();

	static <T extends Enum<T>> T randomItem(Class<T> enumClass) {
		T[] values = enumClass.getEnumConstants();
		return values[ThreadLocalRandom.current().nextInt(values.length)];
	}
}
