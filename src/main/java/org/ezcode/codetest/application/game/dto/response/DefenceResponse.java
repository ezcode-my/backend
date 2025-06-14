package org.ezcode.codetest.application.game.dto.response;

import org.ezcode.codetest.domain.game.model.enums.Grade;
import org.ezcode.codetest.domain.game.model.enums.ItemCategory;
import org.ezcode.codetest.domain.game.model.enums.ItemType;

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
