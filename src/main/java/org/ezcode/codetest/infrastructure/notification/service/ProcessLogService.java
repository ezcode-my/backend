package org.ezcode.codetest.infrastructure.notification.service;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.infrastructure.notification.model.NotificationProcessLog;
import org.ezcode.codetest.infrastructure.notification.model.NotificationProcessLog.ProcessStatus;
import org.ezcode.codetest.infrastructure.notification.repository.NotificationProcessLogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProcessLogService {
	
	private final NotificationProcessLogRepository processLogRepository;

	@Value("${application.notification.max-retries:5}")
	private int maxRetries;

	/**
	 * 메시지 처리 시작 및 중복 저장 검사
	 * @param messageId 메시지 고유 ID
	 * @param payload 메시지 본문
	 * @return 처리를 계속해야 하면 true, 중복 메시지이면 false를 반환
	 */
	@Transactional
	public boolean startProcessing(String messageId, String payload) {
		
		Optional<NotificationProcessLog> existingLogOpt = processLogRepository.findById(messageId);

		if (existingLogOpt.isPresent()) {
			NotificationProcessLog existingLog = existingLogOpt.get();

			// 1. 이미 성공한 경우 -> 중복이므로 처리 중단
			if (existingLog.getStatus() == ProcessStatus.SUCCESS) {
				log.warn("이미 성공적으로 처리된 메시지입니다. messageId={}", messageId);
				return false;
			}

			// 2. 실패했거나, 중간에 멈춘(PENDING) 경우 -> 재처리를 위해 계속 진행
			//    이때는 새 로그를 만드는 대신, 마지막 시도 시간만 업데이트
			log.info("실패했거나 PENDING 상태인 메시지를 재시도합니다. messageId={}", messageId);
			existingLog.updateLastAttempt();
			processLogRepository.save(existingLog);
			return true;
		}

		NotificationProcessLog newLog = NotificationProcessLog.of(messageId, payload);
		processLogRepository.save(newLog);
		return true;
	}

	// 메시지 처리 성공으로 기록
	@Transactional
	public void finishProcessing(String messageId) {

		processLogRepository.findById(messageId).ifPresent(log -> {
			log.markAsSuccess();
			processLogRepository.save(log);
		});
	}

	// 메시지 처리 실패로 기록
	@Transactional
	public void failProcessing(String messageId, String errorMessage) {

		processLogRepository.findById(messageId).ifPresent(log -> {
			log.markAsFailed(errorMessage, maxRetries);
			processLogRepository.save(log);
		});
	}

	// 재시도할 작업 목록 조회
	@Transactional(readOnly = true)
	public List<NotificationProcessLog> findRetryableJobs() {

		return processLogRepository.findByStatusAndRetryCountLessThan(ProcessStatus.FAILED, maxRetries);
	}
}
