package org.ezcode.codetest.infrastructure.openai;

import java.util.List;
import java.util.Map;

import org.ezcode.codetest.application.submission.dto.request.review.ReviewPayload;
import org.springframework.stereotype.Component;

@Component
class OpenAIMessageBuilder {

    private static final String MODEL_NAME = "o4-mini";

    private static final String PREFIX = """
        당신은 코딩 테스트 사이트의 코드 리뷰어입니다.
        아래 **정확히** 이 형식을 지켜 응답하세요:
        '시간 복잡도:', '코드 총평:' 같은 제목은 **제목**과 같이 볼드체로 변환합니다.
        """.stripIndent();

    private static final String SUFFIX = """
        **주의사항**
        1. 절대 코드 전문이나 정답을 알려주지 마세요.
        2. 절대 코드의 일부분을 작성하지 마세요.
        3. 칭찬할 건 칭찬하되 지적할 건 냉정하게 지적하세요.
        4. 늘 존댓말을 사용하세요.
        5. 이모지는 절대 사용하지 마세요.
        6. 다른 형식으로 답변하면 안 됩니다.
        """.stripIndent();

    public Map<String, Object> buildRequestBody(ReviewPayload reviewPayload) {
        String systemPrompt = buildSystemPrompt(reviewPayload.isCorrect());
        String userPrompt = buildUserPrompt(reviewPayload);

        List<Map<String, Object>> messages = List.of(
            Map.of("role", "system", "content", systemPrompt),
            Map.of("role", "user", "content", userPrompt)
        );

        return Map.of(
            "model", MODEL_NAME,
            "messages", messages
        );
    }

    private String buildUserPrompt(ReviewPayload request) {
        return "문제: "
            + request.problemDescription()
            + "\n"
            + "언어: "
            + request.languageName()
            + "\n"
            + "정답 여부: "
            + (request.isCorrect() ? "정답" : "오답")
            + "\n"
            + "```"
            + request.languageName().toLowerCase()
            + "\n"
            + request.sourceCode() + "```";
    }

    private String buildSystemPrompt(boolean isCorrect) {

        String body;
        if (isCorrect) {
            body = """
                - 시간 복잡도: Big-O 표기법으로만 답하세요. **단, N과 M을 같다고 가정하고 n으로 표기하세요.**
                코드에 포함된 중첩 루프(depth)에 따라 O(N^k) 형태로 정확히 표기해주세요.
                **for 루프뿐만 아니라 while 루프도 모두 중첩(depth)에 포함**하여, 코드에 실제로 있는 루프 개수만큼 exponent를 세십시오.
                예) for-for-for ⇒ O(n³), for-for-while ⇒ O(n³), for-for-for-for-while ⇒ O(n⁵)
                \n
                - 코드 총평:
                각 문장은 한 탭(\t) 들여쓰기 + '- '로 시작.
                문장 끝에만 마침표를 붙이세요.
                \n
                - 조금 더 개선할 수 있는 방안:
                각 문장은 한 탭(\t) 들여쓰기 + '- '로 시작.
                문장 끝에만 마침표를 붙이세요.
                """.stripIndent();
        } else {
            body = """
                - 코드 총평:
                각 문장은 한 탭(\t) 들여쓰기 + '- '로 시작.
                문장 끝에만 마침표를 붙이세요.
                \n
                - 공부하면 좋은 키워드:
                1. 첫 번째 키워드
                2. 두 번째 키워드
                3. 세 번째 키워드
                … 필요한 만큼 번호를 늘려주세요.
                """.stripIndent();
        }

        return PREFIX + "\n" + body + "\n" + SUFFIX;
    }

    protected String buildErrorMessage() {
        return "현재 리뷰 생성에 일시적인 문제가 발생했습니다. 잠시 후 다시 시도해 주세요.";
    }
}
