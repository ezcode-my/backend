package org.ezcode.codetest.common.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
public class ExecutorConfig {

	@Bean(name = "judgeSubmissionExecutor")
	public Executor judgeSubmissionExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("submission-");
		executor.initialize();
		return new DelegatingSecurityContextAsyncTaskExecutor(executor);
	}

	@Bean(name = "judgeTestcaseExecutor")
	public Executor judgeTestcaseExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(25);
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("testcase-");
		executor.initialize();
		return new DelegatingSecurityContextAsyncTaskExecutor(executor);
	}
}
