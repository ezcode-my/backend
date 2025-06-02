package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import org.ezcode.codetest.domain.problem.model.entity.UserProblemStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProblemStatusJpaRepository extends JpaRepository<UserProblemStatus,Long> {
}
