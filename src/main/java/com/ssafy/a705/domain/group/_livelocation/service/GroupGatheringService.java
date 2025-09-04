package com.ssafy.a705.domain.group._livelocation.service;

import com.ssafy.a705.domain.group._livelocation.dto.GroupGatheringDto;
import com.ssafy.a705.domain.group._livelocation.repository.GroupGatheringRedisRepository;
import com.ssafy.a705.domain.group._member.service.GroupMemberService;
import com.ssafy.a705.domain.group.entity.Group;
import com.ssafy.a705.domain.group.service.GroupService;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupGatheringService {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final GroupGatheringRedisRepository groupGatheringRedisService;

    public void cacheGroupGathering(Long groupId, CustomUserDetails userDetails) {
        Group group = groupService.getGroup(groupId);
        groupMemberService.memberAuthorityCheck(groupId, userDetails);
        GroupGatheringDto groupGathering = GroupGatheringDto.from(group);
        groupGatheringRedisService.createGroupGathering(groupId, groupGathering);
    }

    public Optional<GroupGatheringDto> getGroupGathering(Long groupId) {
        return groupGatheringRedisService.getGroupGathering(groupId);
    }

}
