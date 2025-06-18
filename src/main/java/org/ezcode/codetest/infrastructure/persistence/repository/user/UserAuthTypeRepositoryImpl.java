package org.ezcode.codetest.infrastructure.persistence.repository.user;

import java.util.List;

import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.entity.UserAuthType;
import org.ezcode.codetest.domain.user.model.enums.AuthType;
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

	//유저가 로그인한 방법들(AuthType을 리스트로 반환)
	@Override
	public List<AuthType> getUserAuthType(User user) {
		return userAuthTypeJpaRepository.findUserAuthTypeByUser(user).stream()
			.map(UserAuthType::getAuthType).toList();
	}

}
