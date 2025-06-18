package org.ezcode.codetest.application.submission.port;

public interface LockManager {

	boolean tryLock(Long userId, Long problemId);

	void releaseLock(Long userId, Long problemId);

}
