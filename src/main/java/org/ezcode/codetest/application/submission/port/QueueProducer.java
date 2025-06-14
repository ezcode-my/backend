package org.ezcode.codetest.application.submission.port;

import org.ezcode.codetest.infrastructure.event.dto.SubmissionMessage;

public interface QueueProducer {

	void enqueue(SubmissionMessage submissionMessage);

}
