package org.ezcode.codetest.application.submission.dto.request.submission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "코드 제출 요청 DTO")
public record CodeSubmitRequest(

    @Schema(description = "언어 ID", example = "1")
    @NotNull(message = "언어 번호는 필수 입력 값입니다.")
    Long languageId,

    @Schema(
        description = "소스 코드",
        example = "public class Main { public static void main(String[] args) { System.out.println(\"Hello World\"); } }"
    )
    @NotBlank(message = "소스 코드는 필수 입력 값입니다.")
    String sourceCode

) {
}
