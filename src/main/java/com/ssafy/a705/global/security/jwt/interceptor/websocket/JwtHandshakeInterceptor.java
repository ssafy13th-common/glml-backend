package com.ssafy.a705.global.security.jwt.interceptor.websocket;

import com.ssafy.a705.global.security.jwt.service.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Slf4j
@RequiredArgsConstructor
@Component("wsJwtHandshakeInterceptor")
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        if (!(request instanceof ServletServerHttpRequest serverRequest)) {
            log.warn("WebSocket 요청이 Servlet 기반이 아닙니다.");
            return false;
        }
        HttpServletRequest httpRequest = serverRequest.getServletRequest();

        return Optional.ofNullable(httpRequest.getParameter("accessToken"))
                .filter(jwtProvider::isTokenValid)
                .flatMap(jwtProvider::extractEmail)
                .map(memberEmail -> {
                    attributes.put("memberEmail", memberEmail);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception exception) {

    }
}
