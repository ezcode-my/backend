package org.ezcode.codetest.application.usermanagement.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "이메일 전송 요청")
public class SendEmailRequest {
    @Schema(description = "인증 완료 후 리다이렉트할 URL")
    private String redirectUrl;
}
