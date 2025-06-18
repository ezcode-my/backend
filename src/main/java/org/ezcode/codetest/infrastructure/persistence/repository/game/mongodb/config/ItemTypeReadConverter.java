package org.ezcode.codetest.infrastructure.persistence.repository.game.mongodb.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.ezcode.codetest.domain.game.model.item.AccessoryType;
import org.ezcode.codetest.domain.game.model.item.DefenceType;
import org.ezcode.codetest.domain.game.model.item.ItemType;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import lombok.NonNull;

@ReadingConverter
public class ItemTypeReadConverter implements Converter<String, ItemType> {

	private final Map<String, ItemType> NAME_TO_ENUM;

	public ItemTypeReadConverter() {

		Map<String, ItemType> map = new HashMap<>();

		for (WeaponType wt : WeaponType.values()) {
			map.put(wt.name(), wt);
		}
		for (DefenceType dt : DefenceType.values()) {
			map.put(dt.name(), dt);
		}
		for (AccessoryType at : AccessoryType.values()) {
			map.put(at.name(), at);
		}

		NAME_TO_ENUM = Collections.unmodifiableMap(map);
	}

	@Override
	public ItemType convert(@NonNull String source) {

		ItemType result = NAME_TO_ENUM.get(source);
		if (result == null) {
			throw new IllegalArgumentException("Unknown ItemType value: " + source);
		}
		return result;
	}
}
