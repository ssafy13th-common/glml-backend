package com.ssafy.a705.global.security.logout.handler;

import com.ssafy.a705.global.common.redis.RedisService;
import com.ssafy.a705.global.security.jwt.service.JwtProvider;
import com.ssafy.a705.global.security.logout.service.BlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtProvider jwtProvider;
    private final BlacklistService blacklistService;
    private final RedisService redisService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
        String accessToken = jwtProvider.extractAccessToken(request).orElse(null);

        if (accessToken != null && jwtProvider.isTokenValid(accessToken)) {
            // refresh token 삭제
            String email = jwtProvider.extractEmail(accessToken).orElse(null);
            redisService.deleteRefreshToken(email);

            // access token 블랙리스트에 등록(더이상 사용 못하게)
            String jti = jwtProvider.getJti(accessToken);
            long ttlSeconds = jwtProvider.getRemainingSeconds(accessToken);
            if (jti != null && ttlSeconds > 0) {
                blacklistService.block(jti, ttlSeconds);
            }
        }
    }
}
