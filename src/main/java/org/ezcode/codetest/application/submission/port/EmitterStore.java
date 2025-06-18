package org.ezcode.codetest.application.submission.port;

import java.util.Optional;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterStore {

	void saveWithCallbacks(String key, SseEmitter emitter);

	Optional<SseEmitter> get(String key);

	SseEmitter getOrElseThrow(String key);

	void remove(String key);

}
