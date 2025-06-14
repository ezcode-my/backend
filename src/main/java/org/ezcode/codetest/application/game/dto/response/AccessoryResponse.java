package org.ezcode.codetest.application.game.dto.response;

import org.ezcode.codetest.domain.game.model.enums.ItemCategory;
import org.ezcode.codetest.domain.game.model.enums.ItemType;

public record AccessoryResponse(
	ItemCategory itemCategory,
	ItemType itemType,
	String       grade,
	String       name,
	String       description,
	Integer      speed,
	Integer      crit,
	Integer      stun,
	Integer      evasion,
	Integer      accuracy
) implements ItemResponse {}
