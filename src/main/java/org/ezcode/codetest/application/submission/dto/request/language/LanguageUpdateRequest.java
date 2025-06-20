package org.ezcode.codetest.application.submission.dto.request.language;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "언어 업데이트 요청")
public record LanguageUpdateRequest(

    @Schema(description = "Judge0에서 사용하는 언어 ID", example = "62")
    @NotNull(message = "Judge0 아이디는 필수 입력 값입니다.")
    Long judge0Id

) {
}
