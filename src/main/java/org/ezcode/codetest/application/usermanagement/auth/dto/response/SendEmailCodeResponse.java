package org.ezcode.codetest.application.usermanagement.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인증코드 전송 성공")
public record SendEmailCodeResponse(
    @Schema(description = "인증코드 전송 성공 메세지")
    String message
) {
    public static SendEmailCodeResponse from(String message) {
        return new SendEmailCodeResponse(message);
    }
}
