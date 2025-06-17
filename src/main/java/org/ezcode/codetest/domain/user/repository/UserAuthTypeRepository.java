package org.ezcode.codetest.domain.user.repository;

import java.util.List;

import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.entity.UserAuthType;
import org.ezcode.codetest.domain.user.model.enums.AuthType;

public interface UserAuthTypeRepository {
	void createUserAuthType(UserAuthType userAuthType);

	List<AuthType> getUserAuthType(User user);
}
