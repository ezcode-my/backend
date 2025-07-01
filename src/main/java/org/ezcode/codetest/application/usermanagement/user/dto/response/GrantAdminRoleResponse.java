package org.ezcode.codetest.application.usermanagement.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Schema(description = "Admin 권한을 부여")
@AllArgsConstructor
public class GrantAdminRoleResponse {
    private final String message;

}
