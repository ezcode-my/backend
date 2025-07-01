package org.ezcode.codetest.infrastructure.event.publisher;

import org.ezcode.codetest.infrastructure.event.dto.submission.response.GitPushStatusResponse;
import org.ezcode.codetest.infrastructure.event.dto.submission.response.ErrorWsResponse;
import org.ezcode.codetest.infrastructure.event.dto.submission.response.SubmissionFinalResultResponse;
import org.ezcode.codetest.infrastructure.event.dto.submission.response.InitTestcaseListResponse;
import org.ezcode.codetest.infrastructure.event.dto.submission.response.JudgeResultResponse;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StompMessageService {

    private final SimpMessagingTemplate messagingTemplate;

    private static final String SUBMISSION_DEST_PREFIX = "/queue/submission/%s";

    public <T> void handleChatRoomListLoad(T roomData, String principalName, String sessionId) {

        SimpMessageHeaderAccessor accessor =
            SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);

        accessor.setLeaveMutable(true);
        accessor.setSessionId(sessionId);

        messagingTemplate.convertAndSendToUser(
            principalName,
            "/queue/chatrooms",
            roomData,
            accessor.getMessageHeaders()
        );
    }

    public <T> void handleChatRoomHistoryLoad(T chatData, String principalName, String sessionId) {

        SimpMessageHeaderAccessor accessor =
            SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);

        accessor.setLeaveMutable(true);
        accessor.setSessionId(sessionId);

        messagingTemplate.convertAndSendToUser(
            principalName,
            "/queue/chat",
            chatData,
            accessor.getMessageHeaders()
        );
    }

    public void sendInitTestcases(
        String sessionKey,
        String principalName,
        List<InitTestcaseListResponse> dataList
    ) {
        messagingTemplate.convertAndSendToUser(
            principalName,
            SUBMISSION_DEST_PREFIX.formatted(sessionKey) + "/init",
            dataList
        );
    }

    public void sendTestcaseResultUpdate(
        String sessionKey,
        String principalName,
        JudgeResultResponse data
    ) {
        messagingTemplate.convertAndSendToUser(
            principalName,
            SUBMISSION_DEST_PREFIX.formatted(sessionKey) + "/case",
            data
        );
    }

    public void sendFinalResult(
        String sessionKey,
        String principalName,
        SubmissionFinalResultResponse data
    ) {
        messagingTemplate.convertAndSendToUser(
            principalName,
            SUBMISSION_DEST_PREFIX.formatted(sessionKey) + "/final",
            data
        );
    }

    public void sendError(
        String sessionKey,
        ErrorWsResponse data
    ) {
        messagingTemplate.convertAndSend(
            SUBMISSION_DEST_PREFIX.formatted(sessionKey) + "/error",
            data
        );
    }

    public void sendGitStatus(
        String sessionKey,
        String principalName,
        GitPushStatusResponse data
    ) {
        messagingTemplate.convertAndSendToUser(
            principalName,
            SUBMISSION_DEST_PREFIX.formatted(sessionKey) + "/git-status",
            data
        );
    }

    public <T> void handleChatMessageBroadcast(T data, Long roomId) {

        messagingTemplate.convertAndSend("/topic/chat/" + roomId, data);
    }

    public <T> void handleChatRoomEntryExitMessage(T messageData, Long roomId) {

        messagingTemplate.convertAndSend("/topic/chat/" + roomId, messageData);
    }

    public <T> void handleChatRoomParticipantCountChange(T roomData) {

        messagingTemplate.convertAndSend("/topic/chatrooms", roomData);
    }
}
