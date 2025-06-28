package org.ezcode.codetest.infrastructure.event.dto.submission.response;

import org.ezcode.codetest.infrastructure.github.model.PushStatus;

public record GitPushStatusResponse(

    String displayMessage

) {
    public GitPushStatusResponse(PushStatus pushStatus) {
        this(pushStatus.getDisplayMessage());
    }
}
