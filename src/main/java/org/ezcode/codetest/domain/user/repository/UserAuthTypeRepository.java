package org.ezcode.codetest.domain.user.repository;

import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.entity.UserAuthType;

public interface UserAuthTypeRepository {
	void createUserAuthType(UserAuthType userAuthType);
}
