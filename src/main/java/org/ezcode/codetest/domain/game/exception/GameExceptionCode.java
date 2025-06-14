package org.ezcode.codetest.domain.game.exception;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameExceptionCode implements ResponseCode {

	INVENTORY_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당 캐릭터의 인벤토리 조회되지 않습니다."),
	ITEM_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당 아이템이 인벤토리에 없습니다."),
	SKILL_NOT_EXISTS(false, HttpStatus.NOT_FOUND, "해당 스킬이 존재하지 않습니다."),
	ITEM_NOT_EXISTS(false, HttpStatus.NOT_FOUND, "해당 아이템이 존재하지 않습니다."),
	CHARACTER_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당 캐릭터가 조회되지 없습니다."),
	NOT_ENOUGH_GOLD(false, HttpStatus.BAD_REQUEST, "도박을 하기에 충분한 골드가 있지 않습니다."),
	SKILL_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당 스킬이 조회되지 없습니다."),
	SKILL_NOT_OWNED(false, HttpStatus.BAD_REQUEST, "해당 스킬을 보유하고 있지 않습니다."),
	SKILL_ALREADY_EQUIPPED(false, HttpStatus.BAD_REQUEST, "해당 스킬을 이미 장착중입니다."),
	SKILL_SLOT_FULL(false, HttpStatus.BAD_REQUEST, "스킬 슬롯이 꽉차있습니다."),
	SKILL_ALREADY_EXISTS(false, HttpStatus.BAD_REQUEST, "이미 존재하는 스킬입니다."),
	ITEM_ALREADY_EXISTS(false, HttpStatus.BAD_REQUEST, "이미 존재하는 아이템입니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}