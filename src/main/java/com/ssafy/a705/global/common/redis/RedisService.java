package com.ssafy.a705.global.common.redis;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private static final String REFRESH_TOKEN_PREFIX = "RT:";

    private final StringRedisTemplate stringRedisTemplate;

    private void setValue(String key, String value, long expireTime) {
        stringRedisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.MILLISECONDS);
    }

    private String getValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    private void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    public void saveRefreshToken(String email, String refreshToken, long expireTime) {
        setValue(REFRESH_TOKEN_PREFIX + email, refreshToken, expireTime);
    }

    public String getRefreshToken(String email) {
        return getValue(REFRESH_TOKEN_PREFIX + email);
    }

    public void deleteRefreshToken(String email) {
        delete(REFRESH_TOKEN_PREFIX + email);
    }

}