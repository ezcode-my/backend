package org.ezcode.codetest.domain.community.repository;

import java.util.Optional;

import org.ezcode.codetest.domain.community.model.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiscussionRepository {

	Optional<Discussion> findById(Long discussionId);

	Discussion save(Discussion discussion);

	Page<Discussion> findAllByProblemId(Long problemId, Pageable pageable);

}
