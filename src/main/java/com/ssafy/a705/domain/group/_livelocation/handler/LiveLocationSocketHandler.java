package com.ssafy.a705.domain.group._livelocation.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.a705.domain.group._livelocation.Exception.SessionCloseFailException;
import com.ssafy.a705.domain.group._livelocation.dto.LiveLocationReq;
import com.ssafy.a705.domain.group._livelocation.service.LiveLocationService;
import com.ssafy.a705.global.security.websocket.WebSocketSessionManager;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class LiveLocationSocketHandler extends TextWebSocketHandler {

    private final LiveLocationService liveLocationService;
    private final ObjectMapper objectMapper;
    private final WebSocketSessionManager sessionManager;

    private static final String ATTR_EMAIL = "memberEmail";
    private static final String ATTR_GROUPID = "groupId";

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String memberEmail = (String) session.getAttributes().get(ATTR_EMAIL);
        if (Objects.isNull(memberEmail)) {
            closeSession(session, CloseStatus.NOT_ACCEPTABLE, "인증되지 않은 사용자");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        Optional.ofNullable((String) session.getAttributes().get(ATTR_EMAIL))
                .ifPresentOrElse(
                        email -> processMessage(session, email, message),
                        () -> closeSession(session, CloseStatus.NOT_ACCEPTABLE,
                                "인증되지 않은 사용자"));
    }

    private void processMessage(WebSocketSession session, String email,
            TextMessage message) {
        parseRequest(message)
                .ifPresentOrElse(
                        req -> {
                            bindGroupAndRegisterIfFirst(session, email, req);
                            liveLocationService.handleLocationUpdate(req, email);
                        },
                        () -> closeSession(session, CloseStatus.BAD_DATA, "JSON 파싱 실패")
                );
    }

    private Optional<LiveLocationReq> parseRequest(TextMessage message) {
        try {
            return Optional.of(objectMapper.readValue(message.getPayload(),
                    LiveLocationReq.class));
        } catch (JsonProcessingException e) {
            log.warn("잘못된 메세지 형식: {}", message.getPayload());
            return Optional.empty();
        }
    }

    private void bindGroupAndRegisterIfFirst(WebSocketSession session, String email,
            LiveLocationReq liveLocationReq) {
        if (session.getAttributes().get(ATTR_GROUPID) == null) {
            session.getAttributes().put(ATTR_GROUPID, liveLocationReq.groupId());
            sessionManager.registerSession(liveLocationReq.groupId(), email, session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("웹소켓 연결 종료: {}", session.getId());
        String email = (String) session.getAttributes().get(ATTR_EMAIL);
        Long groupId = (Long) session.getAttributes().get(ATTR_GROUPID);

        if (!Objects.isNull(email) && !Objects.isNull(groupId)) {
            liveLocationService.cleanup(groupId, email);
        }
    }

    private void closeSession(WebSocketSession session, CloseStatus status, String reason) {
        try {
            String email = (String) session.getAttributes().get(ATTR_EMAIL);
            Long groupId = (Long) session.getAttributes().get(ATTR_GROUPID);
            if (!Objects.isNull(email) && !Objects.isNull(groupId)) {
                sessionManager.unregisterSession(groupId, email);
            }
            session.close(status);
            log.warn("세션 종료: status={}, reason={}, sessionId={}", status, reason, session.getId());
            sessionManager.unregisterSessionBySessionId(session.getId());
        } catch (IOException e) {
            throw new SessionCloseFailException();
        }
    }
}
