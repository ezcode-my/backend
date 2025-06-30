package org.ezcode.codetest.application.usermanagement.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "비밀번호 찾기 인증 응답")
public record VerifyFindPasswordResponse(
    @Schema(description = "인증 번호 성공 응답 메세지")
    String message,

    @Schema(description = "인증 성공 후 발급, 유저 정보를 담은 토큰(email, userId), 유효시간 10분")
    String tempResetToken
) {
    public static VerifyFindPasswordResponse from(String message, String tempResetToken) {
        return new VerifyFindPasswordResponse(message, tempResetToken);
    }
}
