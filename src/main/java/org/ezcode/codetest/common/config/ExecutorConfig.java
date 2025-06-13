package org.ezcode.codetest.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ExecutorConfig {

	@Bean(name = "judgeSubmissionExecutor")
	public ThreadPoolTaskExecutor judgeSubmissionExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("submission-");
		executor.initialize();
		return executor;
	}

	@Bean(name = "judgeTestcaseExecutor")
	public ThreadPoolTaskExecutor judgeTestcaseExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(25);
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("testcase-");
		executor.initialize();
		return executor;
	}
}
