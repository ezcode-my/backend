package org.ezcode.codetest.application.submission.dto.request.review;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "코드 리뷰 요청 DTO")
public record CodeReviewRequest(

    @Schema(description = "언어 ID", example = "62")
    @NotNull(message = "언어 번호는 필수 입력 값입니다.")
    Long languageId,

    @Schema(
        description = "소스 코드",
        example = "public class Main { public static void main(String[] args) { System.out.println(\"Hello\"); } }"
    )
    @NotBlank(message = "소스 코드는 필수 입력 값입니다.")
    String sourceCode,

    @Schema(description = "정답 여부", example = "true")
    @NotNull(message = "정답 여부는 필수 입력 값입니다.")
    Boolean isCorrect

) {
}
