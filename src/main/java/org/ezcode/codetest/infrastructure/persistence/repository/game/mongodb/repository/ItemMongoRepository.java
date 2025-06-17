package org.ezcode.codetest.infrastructure.persistence.repository.game.mongodb.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.item.Item;
import org.ezcode.codetest.domain.game.model.item.ItemCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemMongoRepository extends MongoRepository<Item, String> {

	List<Item> findAllByItemCategory(ItemCategory itemCategory);

	Optional<Item> findByName(String name);

	void deleteByName(String name);

	List<Item> findByIdIn(Collection<String> id);

	boolean existsByName(String name);
}
