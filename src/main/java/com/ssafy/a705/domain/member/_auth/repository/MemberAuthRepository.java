package com.ssafy.a705.domain.member._auth.repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberAuthRepository {

    private static final Duration VERIFY_TTL = Duration.ofMinutes(5);
    private static final String TOKEN_PREFIX = "verify:token:";
    private static final String MEMBER_PREFIX = "verify:member:";
    private static final String RATE_PREFIX = "rate:verify:";

    private final StringRedisTemplate redisTemplate;

    public void createVerificationToken(Long memberId, String token) {
        redisTemplate.opsForValue()
                .set(TOKEN_PREFIX + token, String.valueOf(memberId), VERIFY_TTL);
        redisTemplate.opsForValue().set(MEMBER_PREFIX + memberId, token, VERIFY_TTL);
    }

    public String getToken(Long memberId) {
        return redisTemplate.opsForValue().get(MEMBER_PREFIX + memberId);
    }

    public void deleteToken(String token) {
        redisTemplate.delete(TOKEN_PREFIX + token);
    }

    public void cleanupToken(Long memberId, String token) {
        redisTemplate.delete(TOKEN_PREFIX + token);
        redisTemplate.delete(MEMBER_PREFIX + memberId);
    }

    public String getMemberIdStr(String token) {
        String tokenKey = TOKEN_PREFIX + token;
        return redisTemplate.opsForValue().get(tokenKey);
    }

    public Long getRate(String email) {
        String rateLimitKey = RATE_PREFIX + email;
        return redisTemplate.opsForValue().increment(rateLimitKey);
    }

    public void setRateLimit(String email) {
        String rateLimitKey = RATE_PREFIX + email;
        redisTemplate.expire(rateLimitKey, Duration.ofMinutes(30));
    }
}
