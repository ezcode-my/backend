package org.ezcode.codetest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(
		exclude = {
				org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration.class
		}
)
@EnableJpaAuditing
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CodetestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodetestApplication.class, args);
	}

}
