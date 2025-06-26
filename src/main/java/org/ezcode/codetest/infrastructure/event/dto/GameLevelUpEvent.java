package org.ezcode.codetest.infrastructure.event.dto;

import java.util.List;

public record GameLevelUpEvent(

	Long userId,

	boolean isSolved,

	List<String> problemCategory

) {
}
