package com.ssafy.a705.global.security.websocket;

import com.ssafy.a705.domain.group._livelocation.Exception.SessionCloseFailException;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WebSocketSessionManager {

    private final ConcurrentMap<String, WebSocketSession> sessionsByEmail = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Set<String>> groupToEmails = new ConcurrentHashMap<>();

    public void registerSession(Long groupId, String memberEmail, WebSocketSession session) {
        sessionsByEmail.put(memberEmail, session);
        groupToEmails.computeIfAbsent(groupId, k -> ConcurrentHashMap.newKeySet()).add(memberEmail);
    }

    public void unregisterSession(Long groupId, String memberEmail) {
        sessionsByEmail.remove(memberEmail);
        Set<String> groupEmails = groupToEmails.get(groupId);
        if (groupEmails != null) {
            groupEmails.remove(memberEmail);
        }
    }

    public void unregisterSessionBySessionId(String sessionId) {
        String targetEmail = null;
        WebSocketSession targetSession = null;

        for (Map.Entry<String, WebSocketSession> entry : sessionsByEmail.entrySet()) {
            WebSocketSession socketSession = entry.getValue();
            if (!Objects.isNull(socketSession) && sessionId.equals(socketSession.getId())) {
                targetEmail = entry.getKey();
                targetSession = socketSession;
                break;
            }
        }

        if (Objects.isNull(targetEmail)) {
            return;
        }

        sessionsByEmail.remove(targetEmail);

        for (Map.Entry<Long, Set<String>> entry : groupToEmails.entrySet()) {
            Set<String> emails = entry.getValue();
            if (emails.remove(targetEmail) && emails.isEmpty()) {
                groupToEmails.remove(entry.getKey());
            }
        }

        if (targetSession.isOpen()) {
            try {
                targetSession.close();
            } catch (IOException e) {
                throw new SessionCloseFailException();
            }
        }
    }


    public Set<String> getEmailsInGroup(Long groupId) {
        return groupToEmails.getOrDefault(groupId, Set.of());
    }

    public Optional<WebSocketSession> getSession(String memberEmail) {
        return Optional.ofNullable(sessionsByEmail.get(memberEmail));
    }

    public void closeAllInGroup(Long groupId) {
        Set<String> emails = groupToEmails.getOrDefault(groupId, Set.of());
        emails.forEach(email -> {
            WebSocketSession s = sessionsByEmail.remove(email);
            if (s != null && s.isOpen()) {
                try {
                    s.close();
                } catch (IOException e) {
                    throw new SessionCloseFailException();
                }
            }
        });
        groupToEmails.remove(groupId);
    }

}