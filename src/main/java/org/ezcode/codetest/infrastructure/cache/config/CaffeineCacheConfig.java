package org.ezcode.codetest.infrastructure.cache.config;

import java.time.Duration;
import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CaffeineCacheConfig {

	@Bean
	public CacheManager cacheManager() {
		CaffeineCache countCache = new CaffeineCache("counts",
			Caffeine.newBuilder()
				.expireAfterWrite(Duration.ofMinutes(1))
				.build());

		CaffeineCache historyCache = new CaffeineCache("histories",
			Caffeine.newBuilder()
				.expireAfterWrite(Duration.ofMinutes(1))
				.build());

		CaffeineCache skillCache = new CaffeineCache("skill",
			Caffeine.newBuilder()
				.expireAfterWrite(Duration.ofMinutes(10))
				.build());

		CaffeineCache itemsByCategoryCache = new CaffeineCache("itemsByCategory",
			Caffeine.newBuilder()
				.expireAfterWrite(Duration.ofMinutes(10))
				.build());

		CaffeineCache encountersCache = new CaffeineCache("encounters",
			Caffeine.newBuilder()
				.expireAfterWrite(Duration.ofMinutes(10))
				.build());

		CaffeineCache choiceCache = new CaffeineCache("choices",
			Caffeine.newBuilder()
				.expireAfterWrite(Duration.ofMinutes(10))
				.build());

		SimpleCacheManager manager = new SimpleCacheManager();
		manager.setCaches(
			List.of(skillCache, itemsByCategoryCache, encountersCache, countCache, historyCache, choiceCache));
		return manager;
	}
}
