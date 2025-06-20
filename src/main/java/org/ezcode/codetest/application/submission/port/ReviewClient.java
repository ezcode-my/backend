package org.ezcode.codetest.application.submission.port;

import org.ezcode.codetest.application.submission.dto.request.review.ReviewPayload;
import org.ezcode.codetest.application.submission.model.ReviewResult;

public interface ReviewClient {

    ReviewResult requestReview(ReviewPayload request);

}
