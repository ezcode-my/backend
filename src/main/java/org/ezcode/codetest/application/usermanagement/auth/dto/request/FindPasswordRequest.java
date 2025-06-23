package org.ezcode.codetest.application.usermanagement.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "비밀번호 찾기 요청")
public class FindPasswordRequest {
    private String email;
}
