package org.ezcode.codetest.infrastructure.event.service;

import java.util.Map;

import org.ezcode.codetest.application.submission.port.QueueProducer;
import org.ezcode.codetest.infrastructure.event.dto.SubmissionMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisJudgeQueueProducer implements QueueProducer {

	private final RedisTemplate<String, String> redisTemplate;

	public void enqueue(SubmissionMessage submissionMessage) {
		Map<String, String> map = Map.of(
			"emitterKey", submissionMessage.emitterKey(),
			"problemId", submissionMessage.problemId().toString(),
			"languageId", submissionMessage.languageId().toString(),
			"userId", submissionMessage.userId().toString(),
			"sourceCode", submissionMessage.sourceCode()
		);

		redisTemplate.opsForStream().add("judge-queue", map);
	}
}
