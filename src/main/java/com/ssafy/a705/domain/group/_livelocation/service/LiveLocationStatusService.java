package com.ssafy.a705.domain.group._livelocation.service;

import com.ssafy.a705.domain.group._livelocation.repository.LiveLocationRedisRepository;
import com.ssafy.a705.domain.group._livelocation.repository.LiveLocationStatusRedisRepository;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiveLocationStatusService {

    private final GroupGatheringService groupGatheringService;
    private final LiveLocationStatusRedisRepository liveLocationStatusRedisRepository;
    private final LiveLocationRedisRepository liveLocationRedisRepository;

    public void enableStatus(Long groupId, CustomUserDetails userDetails) {
        groupGatheringService.cacheGroupGathering(groupId, userDetails);
        liveLocationStatusRedisRepository.enable(groupId, userDetails.getEmail());
        liveLocationRedisRepository.addPendingMember(groupId, userDetails.getEmail());
    }

    public void disableStatus(Long groupId, CustomUserDetails userDetails) {
        liveLocationStatusRedisRepository.disable(groupId, userDetails.getEmail());
        liveLocationRedisRepository.removePendingMember(groupId, userDetails.getEmail());
    }

    public void disableStatus(Long groupId, String memberEmail) {
        liveLocationStatusRedisRepository.disable(groupId, memberEmail);
        liveLocationRedisRepository.removePendingMember(groupId, memberEmail);
    }

    public boolean isEnabledStatus(Long groupId, String memberEmail) {
        log.info("isEnabledStatus groudId: {}, memberEmail: {}", groupId, memberEmail);
        return liveLocationStatusRedisRepository.isEnabled(groupId, memberEmail);
    }

}
