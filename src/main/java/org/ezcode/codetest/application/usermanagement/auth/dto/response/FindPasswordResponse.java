package org.ezcode.codetest.application.usermanagement.auth.dto.response;

public record FindPasswordResponse(
    String message
) {
    public static FindPasswordResponse from(String message) {
        return new FindPasswordResponse(message);
    }
}
