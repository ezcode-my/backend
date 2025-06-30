package org.ezcode.codetest.application.usermanagement.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "비밀번호 리셋을 위한 입력")
public record ResetPasswordRequest (
    @Schema(description = "비밀번호 리셋 유저의 토큰 - 유효 시간 10분")
    String tempResetToken,

    @Schema(description = "변경할 비밀번호", example = "myPassword@@!")
    String newPassword,

    @Schema(description = "변경할 비밀번호 확인용 재입력", example = "myPassword@@!")
    String newPasswordConfirm
){
    public static ResetPasswordRequest from(String tempResetToken, String newPassword, String newPasswordConfirm) {
        return new ResetPasswordRequest(tempResetToken, newPassword, newPasswordConfirm);
    }
}

