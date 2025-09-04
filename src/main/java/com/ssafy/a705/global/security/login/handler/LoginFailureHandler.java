package com.ssafy.a705.global.security.login.handler;

import com.ssafy.a705.global.security.jwt.service.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final JwtProvider jwtProvider;

    public LoginFailureHandler(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) {
        log.info("로그인 실패 {}", exception.getMessage());
        jwtProvider.sendErrorResponse(response, "로그인 실패",
                HttpServletResponse.SC_UNAUTHORIZED);
    }
}
