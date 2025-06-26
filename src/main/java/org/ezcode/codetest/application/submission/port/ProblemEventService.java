package org.ezcode.codetest.application.submission.port;

import org.ezcode.codetest.domain.submission.model.SubmissionResult;

public interface ProblemEventService {

    void publishProblemSolveEvent(SubmissionResult event);

}
