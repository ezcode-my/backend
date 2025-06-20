package org.ezcode.codetest.application.submission.dto.response.submission;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.submission.model.entity.Submission;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"problemId", "problemDescription", "submissions"})
@Schema(description = "문제 단위로 묶은 제출 목록 응답")
public class GroupedSubmissionResponse {

    @Schema(description = "문제 ID", example = "10")
    private final Long problemId;

    @Schema(description = "문제 설명", example = "두 수의 합을 구하는 문제입니다.")
    private final String problemDescription;

    @Schema(description = "해당 문제에 대한 제출 목록")
    private final List<SubmissionDetailResponse> submissions;

    public static List<GroupedSubmissionResponse> groupByProblem(List<Submission> submissions) {
        return submissions.stream()
            .collect(Collectors.groupingBy(Submission::getProblem))
            .entrySet()
            .stream()
            .map(entry -> createSorted(entry.getKey(), entry.getValue()))
            .toList();
    }

    private static GroupedSubmissionResponse createSorted(Problem problem, List<Submission> submissions) {
        List<Submission> sorted = submissions.stream()
            .sorted(Comparator.comparing(Submission::getCreatedAt).reversed())
            .toList();

        return new GroupedSubmissionResponse(problem, sorted);
    }

    private GroupedSubmissionResponse(Problem problem, List<Submission> submissions) {
        this.problemId = problem.getId();
        this.problemDescription = problem.getDescription();
        this.submissions = submissions.stream()
            .map(SubmissionDetailResponse::from)
            .toList();
    }
}
