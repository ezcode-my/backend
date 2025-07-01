package org.ezcode.codetest.infrastructure.notification.repository;

import java.util.Optional;

import org.ezcode.codetest.infrastructure.notification.model.NotificationDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationMongoRepository extends MongoRepository<NotificationDocument, String> {

	Page<NotificationDocument> findAllByPrincipalName(String principalName, Pageable pageable);

	Optional<NotificationDocument> findByIdAndPrincipalName(String id, String principalName);

}
