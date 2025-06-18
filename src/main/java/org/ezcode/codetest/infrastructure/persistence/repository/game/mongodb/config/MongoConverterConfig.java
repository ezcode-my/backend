package org.ezcode.codetest.infrastructure.persistence.repository.game.mongodb.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
public class MongoConverterConfig {

	@Bean
	public MongoCustomConversions customConversions() {
		return new MongoCustomConversions(List.of(new ItemTypeReadConverter()));
	}
}
