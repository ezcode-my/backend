package org.ezcode.codetest.infrastructure.sse;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.ezcode.codetest.application.submission.port.EmitterStore;
import org.ezcode.codetest.domain.submission.exception.SubmissionException;
import org.ezcode.codetest.domain.submission.exception.code.SubmissionExceptionCode;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InMemoryEmitterStore implements EmitterStore {

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    @Override
    public void saveWithCallbacks(String key, SseEmitter emitter) {
        emitterMap.put(key, emitter);

        emitter.onCompletion(() -> log.info("[SSE 완료] 정상 종료됨"));

        emitter.onTimeout(() -> {
            log.warn("[SSE 타임아웃] 연결 시간이 초과되었습니다");
            emitter.completeWithError(new SubmissionException(SubmissionExceptionCode.EMITTER_SEND_ERROR));
            remove(key);
        });

        emitter.onError(e -> {
            log.error("[SSE 에러 발생] 예외: {}", e.toString(), e);
            remove(key);
        });

    }

    @Override
    public Optional<SseEmitter> get(String key) {
        return Optional.ofNullable(emitterMap.get(key));
    }

    @Override
    public SseEmitter getOrElseThrow(String key) {
        return Optional.ofNullable(emitterMap.get(key))
            .orElseThrow(() -> new SubmissionException(SubmissionExceptionCode.EMITTER_NOT_FOUND));
    }

    @Override
    public void remove(String key) {
        emitterMap.remove(key);
    }
}
