package org.ezcode.codetest.application.usermanagement.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResetPasswordRequest {
    private String email;
    private String newPassword;
    private String token;
}
