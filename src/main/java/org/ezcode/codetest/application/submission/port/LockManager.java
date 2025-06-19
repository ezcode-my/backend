package org.ezcode.codetest.application.submission.port;

public interface LockManager {

	boolean tryLock(String prefix, Long userId, Long problemId);

	void releaseLock(String prefix, Long userId, Long problemId);

}
