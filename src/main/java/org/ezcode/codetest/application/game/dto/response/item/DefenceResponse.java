package org.ezcode.codetest.application.game.dto.response.item;

import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.ItemCategory;
import org.ezcode.codetest.domain.game.model.item.ItemType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "방어구에 대한 전반적인 설명")
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
