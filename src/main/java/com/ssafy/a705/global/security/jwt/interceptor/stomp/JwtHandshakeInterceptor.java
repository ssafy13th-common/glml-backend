package com.ssafy.a705.global.security.jwt.interceptor.stomp;

import com.ssafy.a705.global.security.jwt.exception.MissingEmailException;
import com.ssafy.a705.global.security.jwt.service.JwtProvider;
import java.security.Principal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@RequiredArgsConstructor
@Component("stompJwtHandshakeInterceptor")
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            String token = servletRequest.getServletRequest().getParameter("token");
            if (token == null) {
                token = servletRequest.getServletRequest().getHeader("Authorization");
            }

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                if (jwtProvider.isTokenValid(token)) {
                    String email = jwtProvider.extractEmail(token).orElseThrow(
                            MissingEmailException::new);
                    attributes.put("principal", (Principal) () -> email);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception exception) {

    }
}
