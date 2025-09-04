package com.ssafy.a705.domain.group._livelocation.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.a705.domain.group._livelocation.Exception.DeserializationException;
import com.ssafy.a705.domain.group._livelocation.Exception.SerializationException;
import com.ssafy.a705.domain.group._livelocation.dto.LiveLocationRes;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LiveLocationRedisRepository {

    private static final String LIVE_LOCATION_FMT = "LIVE_LOCATION:GROUP:%d:LIVELOCATION:MEMBERS:%s";
    private static final String LIVE_LOCATIONS_FMT = "LIVE_LOCATION:GROUP:%d:LIVELOCATION:MEMBERS:*";
    private static final String PENDING_FMT = "LIVE_LOCATION:GROUP:%d:PENDING:MEMBERS:";
    private static final String GROUP_FMT = "LIVE_LOCATION:GROUP:%d:*";

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void saveLocation(LiveLocationRes liveLocationRes) {
        String key = getLiveLocationKey(liveLocationRes.groupId(), liveLocationRes.memberEmail());
        String value = serializeLocation(liveLocationRes);
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public List<LiveLocationRes> getAllLocationInGroup(Long groupId) {
        String pattern = String.format(LIVE_LOCATIONS_FMT, groupId);
        Set<String> keys = stringRedisTemplate.keys(pattern);
        List<String> values = stringRedisTemplate.opsForValue().multiGet(keys);
        return values.stream()
                .filter(Objects::nonNull)
                .map(this::deserializeLocation)
                .filter(Objects::nonNull)
                .toList();
    }

    public void deleteLocation(Long groupId, String memberEmail) {
        redisTemplate.delete(getLiveLocationKey(groupId, memberEmail));
    }

    public void deleteAllLocationInGroup(Long groupId) {
        String pattern = String.format(LIVE_LOCATIONS_FMT, groupId);
        Set<String> keys = stringRedisTemplate.keys(pattern);
        keys.forEach(stringRedisTemplate::delete);
    }

    public void addPendingMember(Long groupId, String memberEmail) {
        stringRedisTemplate.opsForSet().add(getPendingKey(groupId), memberEmail);
    }

    public long countPendingMembers(Long groupId) {
        Long size = stringRedisTemplate.opsForSet().size(getPendingKey(groupId));
        return size == null ? 0L : size;
    }

    public void removePendingMember(Long groupId, String memberEmail) {
        stringRedisTemplate.opsForSet().remove(getPendingKey(groupId), memberEmail);
    }

    public void cleanupGroupAllKeys(Long groupId) {
        String pattern = String.format(GROUP_FMT, groupId);
        Set<String> keys = stringRedisTemplate.keys(pattern);
        if (!keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }
    }

    private String getLiveLocationKey(Long groupId, String memberEmail) {
        return String.format(LIVE_LOCATION_FMT, groupId, memberEmail);
    }

    private String getPendingKey(Long groupId) {
        return String.format(PENDING_FMT, groupId);
    }

    private String serializeLocation(LiveLocationRes liveLocationRes) {
        try {
            return objectMapper.writeValueAsString(liveLocationRes);
        } catch (JsonProcessingException e) {
            throw new SerializationException();
        }
    }

    private LiveLocationRes deserializeLocation(String json) {
        try {
            return objectMapper.readValue(json, LiveLocationRes.class);
        } catch (JsonProcessingException e) {
            throw new DeserializationException();
        }
    }
}