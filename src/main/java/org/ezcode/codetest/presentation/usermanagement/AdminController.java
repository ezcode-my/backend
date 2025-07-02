package org.ezcode.codetest.presentation.usermanagement;

import org.ezcode.codetest.application.usermanagement.user.dto.response.GrantAdminRoleResponse;
import org.ezcode.codetest.application.usermanagement.user.service.AdminService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "관리자(Admin) 전용 기능", description = "관리자 권한을 가진 유저만 접근 가능한 기능입니다")
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "관리자로 전환", description = "관리자 권한을 가지고 있는 유저는 다른 유저의 권한을 관리자로 수정할 수 있습니다.")
    @PostMapping("/users/{userId}/grant-admin")
    public ResponseEntity<GrantAdminRoleResponse> grantAdminRole(
        @AuthenticationPrincipal AuthUser authUser,
        @PathVariable Long userId
    ){
        return ResponseEntity.status(HttpStatus.OK).body(adminService.grantAdminRole(authUser, userId));
    }
}
