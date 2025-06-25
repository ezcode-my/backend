package org.ezcode.codetest.application.submission.port;

import org.ezcode.codetest.domain.submission.model.entity.UserProblemResult;

public interface ProblemEventService {

    void publishProblemSolveEvent(UserProblemResult event);

}
