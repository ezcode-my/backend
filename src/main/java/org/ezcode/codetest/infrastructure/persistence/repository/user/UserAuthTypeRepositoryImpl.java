package org.ezcode.codetest.infrastructure.persistence.repository.user;

import org.ezcode.codetest.domain.user.model.entity.UserAuthType;
import org.ezcode.codetest.domain.user.repository.UserAuthTypeRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserAuthTypeRepositoryImpl implements UserAuthTypeRepository {
	private final UserAuthTypeJpaRepository userAuthTypeJpaRepository;

	@Override
	public void createUserAuthType(UserAuthType userAuthType) {
		userAuthTypeJpaRepository.save(userAuthType);
	}
}
