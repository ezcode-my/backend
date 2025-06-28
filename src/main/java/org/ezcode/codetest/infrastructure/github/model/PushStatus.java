package org.ezcode.codetest.infrastructure.github.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PushStatus {

    STARTED("푸시 시작"),

    SUCCESS("푸시 성공"),

    FAILED("푸시 실패 - 잠시 후 다시 시도해주세요.");

    private final String displayMessage;
}
