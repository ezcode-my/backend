package org.ezcode.codetest.application.submission.dto.response.submission;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.Testcase;

public record SubmitResponse(

    String sessionKey,

    List<Long> testcaseIds

) {
   public static SubmitResponse of(String sessionKey, List<Testcase> testcases) {
       return new SubmitResponse(sessionKey, testcases.stream().map(Testcase::getId).toList());
   }
}
