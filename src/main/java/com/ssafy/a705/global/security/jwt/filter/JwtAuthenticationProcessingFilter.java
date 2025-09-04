package com.ssafy.a705.global.security.jwt.filter;

import com.ssafy.a705.domain.member.dto.TokenRes;
import com.ssafy.a705.global.common.redis.RedisService;
import com.ssafy.a705.global.security.jwt.service.JwtProvider;
import com.ssafy.a705.global.security.logout.service.BlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/api/v1/auth/login";

    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    private final UserDetailsService userDetailsService;
    private final BlacklistService blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURL().toString().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtProvider.extractAccessToken(request).orElse(null);

        if (!Objects.isNull(accessToken)) {
            String email = jwtProvider.extractEmail(accessToken).orElse(null);
            if (Objects.isNull(email)) {
                jwtProvider.sendErrorResponse(response, "Access Token이 유효하지 않습니다.(클레임 누락)",
                        HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String jti = jwtProvider.getJti(accessToken);
            if (!Objects.isNull(jti) && blacklistService.isBlocked(jti)) {
                jwtProvider.sendErrorResponse(response, "로그아웃 된 유저입니다.",
                        HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            if (jwtProvider.isExpired(accessToken)) {
                String refreshToken = jwtProvider.extractRefreshToken(request).orElse(null);
                if (!Objects.isNull(refreshToken)) {
                    email = jwtProvider.extractEmail(refreshToken).orElse(null);
                    if (Objects.isNull(email)) {
                        jwtProvider.sendErrorResponse(response, "Refresh Token이 유효하지 않습니다.(클레임 누락)",
                                HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }

                    String storedToken = redisService.getRefreshToken(email);
                    log.info("stored: {}", storedToken);
                    log.info("refresh: {}", refreshToken);
                    if (!refreshToken.equals(storedToken)) {
                        jwtProvider.sendErrorResponse(response, "Refresh Token이 일치하지 않습니다",
                                HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                    reIssueTokens(response, email);
                    return;
                }
            }

            setAuthentication(email);
            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void reIssueTokens(HttpServletResponse response, String email) {
        // RTR 방식
        String accessToken = jwtProvider.createAccessToken(email);
        String refreshToken = jwtProvider.createRefreshToken(email);

        redisService.deleteRefreshToken(email);
        redisService.saveRefreshToken(email, refreshToken,
                jwtProvider.getRefreshTokenExpirationPeriod());

        TokenRes tokenRes = TokenRes.of(accessToken, refreshToken);
        jwtProvider.sendTokensInResponse(response, tokenRes);
    }

    private void setAuthentication(String email) throws UsernameNotFoundException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}