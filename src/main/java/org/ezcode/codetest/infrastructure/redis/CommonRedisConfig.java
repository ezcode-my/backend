package org.ezcode.codetest.infrastructure.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.resource.DefaultClientResources;

@Configuration
public class CommonRedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Bean(destroyMethod = "shutdown")
    public DefaultClientResources clientResources() {
        return DefaultClientResources.create();
    }

    @Bean
    @Primary
    public LettuceConnectionFactory redisConnectionFactory(DefaultClientResources clientResources) {
        ClientOptions clientOptions = ClientOptions.builder()
            .autoReconnect(true)
            .pingBeforeActivateConnection(true)
            .socketOptions(SocketOptions.builder()
                .keepAlive(true)
                .build())
            .build();

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
            .clientResources(clientResources)
            .clientOptions(clientOptions)
            .build();

        RedisStandaloneConfiguration redisConfig =
            new RedisStandaloneConfiguration(redisHost, redisPort);

        redisConfig.setPassword(redisPassword);

        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }
}
