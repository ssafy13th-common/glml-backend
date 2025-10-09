package com.ssafy.a705.group.infrastructure.livelocation.service;

import com.ssafy.a705.group.presentation.livelocation.dto.GroupGatheringDto;
import com.ssafy.a705.group.infrastructure.livelocation.respository.GroupGatheringRedisRepository;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.group.application.participant.ParticipantApplicationService;
import com.ssafy.a705.group.domain.group.entity.Group;
import com.ssafy.a705.group.domain.group.service.GroupReader;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupGatheringService {

    private final GroupReader groupReader;
    private final ParticipantApplicationService participantService;
    private final GroupGatheringRedisRepository groupGatheringRedisService;

    public void cacheGroupGathering(Long groupId, CustomUserDetails userDetails) {
        Group group = groupReader.getGroup(groupId);
        participantService.memberAuthorityCheck(groupId, userDetails);
        GroupGatheringDto groupGathering = GroupGatheringDto.from(group);
        groupGatheringRedisService.createGroupGathering(groupId, groupGathering);
    }

    public Optional<GroupGatheringDto> getGroupGathering(Long groupId) {
        return groupGatheringRedisService.getGroupGathering(groupId);
    }

}
