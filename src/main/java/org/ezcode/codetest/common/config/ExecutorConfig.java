package org.ezcode.codetest.common.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class ExecutorConfig {

    @Bean(name = "consumerExecutor")
    public Executor consumerExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("consumer-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "judgeSubmissionExecutor")
    public Executor judgeSubmissionExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("submission-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "judgeTestcaseExecutor")
    public Executor judgeTestcaseExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(25);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("testcase-");
        executor.initialize();
        return executor;
    }
}
