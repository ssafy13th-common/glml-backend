package com.ssafy.a705.domain.group._livelocation.repository;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LiveLocationStatusRedisRepository {

    private static final String LIVE_LOCATION_PREFIX = "LIVE_LOCATION:GROUP:%d:STATUS:MEMBER:%s";

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${app.live-location.ttl-millis}")
    private long ttlMillis;

    public void enable(Long groupId, String memberEmail) {
        String key = getStatusKey(groupId, memberEmail);
        stringRedisTemplate.opsForValue().set(key, "true", ttlMillis, TimeUnit.MILLISECONDS);
    }

    public void disable(Long groupId, String memberEmail) {
        String key = getStatusKey(groupId, memberEmail);
        stringRedisTemplate.delete(key);
    }

    public Boolean isEnabled(Long groupId, String memberEmail) {
        String key = getStatusKey(groupId, memberEmail);
        String value = redisTemplate.opsForValue().get(key);
        return Boolean.parseBoolean(value);
    }

    private String getStatusKey(Long groupId, String memberEmail) {
        return String.format(LIVE_LOCATION_PREFIX, groupId, memberEmail);
    }
}
