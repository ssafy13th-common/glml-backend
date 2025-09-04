package com.ssafy.a705.domain.group._livelocation.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.a705.domain.group._livelocation.Exception.DeserializationException;
import com.ssafy.a705.domain.group._livelocation.Exception.SerializationException;
import com.ssafy.a705.domain.group._livelocation.dto.GroupGatheringDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GroupGatheringRedisRepository {

    private static final String LIVE_LOCATION_PREFIX = "LIVE_LOCATION:GROUP:%d:GATHERING:";

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void createGroupGathering(Long groupId, GroupGatheringDto groupGatheringDto) {
        String key = getGroupGatheringKey(groupId);
        String value = serializeGroupGathering(groupGatheringDto);
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public Optional<GroupGatheringDto> getGroupGathering(Long groupId) {
        String key = getGroupGatheringKey(groupId);
        String value = stringRedisTemplate.opsForValue().get(key);
        return Optional.ofNullable(value)
                .map(this::deserializeGroupGathering);
    }

    public void deleteGroupGathering(Long groupId) {
        redisTemplate.delete(getGroupGatheringKey(groupId));
    }

    private String getGroupGatheringKey(Long groupId) {
        return String.format(LIVE_LOCATION_PREFIX, groupId);
    }

    private String serializeGroupGathering(GroupGatheringDto groupGatheringDto) {
        try {
            return objectMapper.writeValueAsString(groupGatheringDto);
        } catch (JsonProcessingException e) {
            throw new SerializationException();
        }
    }

    private GroupGatheringDto deserializeGroupGathering(String json) {
        try {
            return objectMapper.readValue(json, GroupGatheringDto.class);
        } catch (JsonProcessingException e) {
            throw new DeserializationException();
        }
    }
}
