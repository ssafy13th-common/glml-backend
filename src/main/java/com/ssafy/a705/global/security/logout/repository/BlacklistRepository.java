package com.ssafy.a705.global.security.logout.repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BlacklistRepository {

    private final StringRedisTemplate redisTemplate;

    public void deleteTokenByEmail(String jti, long ttlSeconds) {
        String key = "BLACKLIST:" + jti;
        redisTemplate.opsForValue().set(key, "1", Duration.ofSeconds(ttlSeconds));
    }

    public boolean hasKey(String jti) {
        return redisTemplate.hasKey("BLACKLIST:" + jti);
    }
}
