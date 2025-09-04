package com.ssafy.a705.global.security.jwt.interceptor.stomp;

import com.ssafy.a705.global.security.jwt.exception.InvalidTokenException;
import com.ssafy.a705.global.security.jwt.exception.MissingEmailException;
import com.ssafy.a705.global.security.jwt.service.JwtProvider;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompJwtInterceptor implements ChannelInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.SEND.equals(accessor.getCommand())) {
            Principal user = (Principal) message.getHeaders()
                    .get(SimpMessageHeaderAccessor.USER_HEADER);
            log.info(user.getName());
        }
        log.info("STOMP Command: {}", accessor.getCommand());
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            log.info("CONNECT received. Token: {}", token);
            if (token != null && jwtProvider.isTokenValid(token.replace("Bearer ", ""))) {
                String userEmail = jwtProvider.extractEmail(token.replace("Bearer ", ""))
                        .orElseThrow(MissingEmailException::new);
                log.info("JWT valid. User: {}", userEmail);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userEmail, null, List.of());
                accessor.setUser(authentication);
                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
                log.info(accessor.getUser().getName());
            } else {
                log.error("Invalid token: {}", token);
                throw new InvalidTokenException();
            }
        }
        return message;
    }
}
