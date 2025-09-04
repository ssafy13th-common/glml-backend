package com.ssafy.a705.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.a705.global.common.controller.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        ApiResponse<Object> res = ApiResponse.failedOf("인증되지 않은 사용자입니다");
        String json = objectMapper.writeValueAsString(res);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(String.valueOf(json));
    }
}