package org.ezcode.codetest.infrastructure.persitence.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ezcode.codetest.domain.user.model.entity.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
