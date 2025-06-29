package org.ezcode.codetest.application.usermanagement.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "깃허브 자동 Push 여부 변경 후 응답")
public class UserGitubAutoPushResponse {
    @Schema(description = "메세지", example = "변경되었습니다")
    private final String message;
    @Schema(description = "현재 상태", example = "true")
    private final boolean gitPushStatus;
}
