package com.ssafy.a705.domain.group._livelocation.config;

import com.ssafy.a705.domain.group._livelocation.handler.LiveLocationSocketHandler;
import com.ssafy.a705.global.security.jwt.interceptor.websocket.JwtHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket
@RequiredArgsConstructor
@Configuration("liveLocationWebSocketConfig")
public class LiveLocationWebSocketConfig implements WebSocketConfigurer {

    private final @Qualifier("wsJwtHandshakeInterceptor") JwtHandshakeInterceptor jwtHandshakeInterceptor;
    private final LiveLocationSocketHandler liveLocationSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(liveLocationSocketHandler, "/ws/live-location")
                .addInterceptors(jwtHandshakeInterceptor)
                .setAllowedOrigins("*");
    }
}
