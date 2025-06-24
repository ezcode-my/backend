package org.ezcode.codetest.infrastructure.event.dto;

public record GameLevelUpEvent(

	Long userId,

	boolean isProblemSolved,

	String problemCategory

) {
}
