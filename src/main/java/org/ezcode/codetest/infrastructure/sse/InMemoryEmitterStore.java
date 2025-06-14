package org.ezcode.codetest.infrastructure.sse;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.ezcode.codetest.application.submission.port.EmitterStore;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class InMemoryEmitterStore implements EmitterStore {

	private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

	@Override
	public void save(String key, SseEmitter emitter) {
		emitterMap.put(key, emitter);
		emitter.onCompletion(() -> emitterMap.remove(key));
		emitter.onTimeout(() -> emitterMap.remove(key));
		emitter.onError((e) -> emitterMap.remove(key));
	}

	@Override
	public Optional<SseEmitter> get(String key) {
		return Optional.ofNullable(emitterMap.get(key));
	}

	@Override
	public void remove(String key) {
		emitterMap.remove(key);
	}
}
