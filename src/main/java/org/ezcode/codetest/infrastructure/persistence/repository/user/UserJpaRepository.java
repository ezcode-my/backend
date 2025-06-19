package org.ezcode.codetest.infrastructure.persistence.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
	User findUserByEmail(String email);

	boolean existsByNickname(String nickname);

	Optional<User> findById(Long id);

	@Modifying(clearAutomatically = true)
	@Query("update User u set u.reviewToken = :newToken where u.id in :ids")
	void updateReviewTokens(
		@Param("ids") List<Long> ids,
		@Param("newToken") int newToken
	);
}
