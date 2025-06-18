package org.ezcode.codetest.application.game.dto.response.item;

import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.ItemCategory;
import org.ezcode.codetest.domain.game.model.item.ItemType;

public record DefenceResponse(
	ItemCategory itemCategory,
	ItemType itemType,
	Grade grade,
	String name,
	String description,
	Integer def,
	Integer speed,
	Integer evasion
) implements ItemResponse {
}
