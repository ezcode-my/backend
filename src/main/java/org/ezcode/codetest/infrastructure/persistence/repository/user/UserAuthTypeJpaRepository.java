package org.ezcode.codetest.infrastructure.persistence.repository.user;

import java.util.List;

import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.entity.UserAuthType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthTypeJpaRepository extends JpaRepository<UserAuthType, Long> {
	List<UserAuthType> findUserAuthTypeByUser(User user);

}
