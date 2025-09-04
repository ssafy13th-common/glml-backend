package com.ssafy.a705.global.security.jwt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.a705.domain.member.dto.TokenRes;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.common.controller.ApiResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secretKey}")
    private String secretKeyPlain;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private SecretKey secretKey;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";

    private final MemberRepository memberRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(secretKeyPlain.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String email) {
        Date now = new Date();
        String jti = UUID.randomUUID().toString();
        return Jwts.builder()
                .subject(ACCESS_TOKEN_SUBJECT)
                .id(jti)
                .claim(EMAIL_CLAIM, email)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + accessTokenExpirationPeriod))
                .signWith(secretKey, SIG.HS512)
                .compact();
    }

    public String createRefreshToken(String email) {
        Date now = new Date();
        return Jwts.builder()
                .subject(REFRESH_TOKEN_SUBJECT)
                .claim(EMAIL_CLAIM, email)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .signWith(secretKey, SIG.HS512)
                .compact();
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(token -> token.startsWith(BEARER))
                .map(token -> token.substring(BEARER.length()));
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(token -> token.startsWith(BEARER))
                .map(token -> token.substring(BEARER.length()));
    }

    public Optional<String> extractEmail(String accessToken) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
            return Optional.ofNullable(claims.get(EMAIL_CLAIM, String.class));
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            return Optional.ofNullable(claims.get(EMAIL_CLAIM, String.class));
        } catch (JwtException e) {
            return Optional.empty();
        }
    }

    public void sendTokensInResponse(HttpServletResponse response, TokenRes tokenRes) {
        try {
            setAccessToken(response, tokenRes.accessToken());
            setRefreshToken(response, tokenRes.refreshToken());

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ApiResponse<Void> apiResponse = ApiResponse.of();

            String responseBody = objectMapper.writeValueAsString(apiResponse);
            response.getWriter().write(responseBody);
        } catch (IOException e) {
            log.error("토큰 응답 중 오류 발생 {}", e.getMessage());
        }
    }

    public void sendErrorResponse(HttpServletResponse response, String message, int status) {
        try {
            response.setStatus(status);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ApiResponse<Void> errorRes = ApiResponse.failedOf(message);

            String responseBody = new ObjectMapper().writeValueAsString(errorRes);
            response.getWriter().write(responseBody);
        } catch (IOException e) {
            log.error("응답 쓰기 실패 {}", e.getMessage());
        }
    }

    private void setAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, BEARER + accessToken);
    }

    private void setRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parse(token);
            return true;
        } catch (JwtException e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

    public boolean isExpired(String token) {
        try {
            Date exp = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            return exp != null && exp.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public String getJti(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                    .getId();
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            return claims.getId();
        }
    }

    public long getRemainingSeconds(String token) {
        Date exp = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .getExpiration();
        long sec = (exp.getTime() - System.currentTimeMillis()) / 1000;
        return Math.max(sec, 0);
    }


}