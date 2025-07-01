package org.ezcode.codetest.application.usermanagement.user.service;

import org.ezcode.codetest.application.usermanagement.user.dto.response.GrantAdminRoleResponse;
import org.ezcode.codetest.domain.user.exception.AdminException;
import org.ezcode.codetest.domain.user.exception.code.AdminExceptionCode;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.enums.UserRole;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserDomainService userDomainService;

    @Transactional
    public GrantAdminRoleResponse grantAdminRole(AuthUser authUser, Long userId) {
        if (authUser.getId().equals(userId)) {
            throw new AdminException(AdminExceptionCode.GRANT_ADMIN_SELF);
        }
        User user = userDomainService.getUserById(userId);
        if (user.getRole().equals(UserRole.ADMIN)) {
            throw new AdminException(AdminExceptionCode.ALREADY_ADMIN_USER);
        }
        user.modifyUserRole(UserRole.ADMIN);

        return new GrantAdminRoleResponse("ADMIN 권한을 부여합니다");
    }
}
