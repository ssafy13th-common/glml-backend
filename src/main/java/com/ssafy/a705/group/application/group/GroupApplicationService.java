package com.ssafy.a705.group.application.group;

import com.ssafy.a705.domain.chat.dto.request.CreateRoomReq;
import com.ssafy.a705.domain.chat.dto.response.ChatRoomRes;
import com.ssafy.a705.domain.chat.service.ChatRoomService;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.group.application.participant.ParticipantApplicationService;
import com.ssafy.a705.group.domain.group.entity.Group;
import com.ssafy.a705.group.domain.group.service.GroupReader;
import com.ssafy.a705.group.domain.group.service.GroupStore;
import com.ssafy.a705.group.domain.participant.service.ParticipantReader;
import com.ssafy.a705.group.presentation.group.dto.request.GroupReq;
import com.ssafy.a705.group.presentation.group.dto.request.GroupUpdateReq;
import com.ssafy.a705.group.presentation.group.dto.response.GroupGatheringRes;
import com.ssafy.a705.group.presentation.group.dto.response.GroupInfoRes;
import com.ssafy.a705.group.presentation.group.dto.response.GroupsRes;
import com.ssafy.a705.group.presentation.participant.dto.response.ParticipantProfileRes;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupApplicationService {

    private final GroupReader groupReader;
    private final GroupStore groupStore;
    private final ChatRoomService chatRoomService;
    private final ParticipantApplicationService participantService;
    private final ParticipantReader participantReader;

    @Transactional
    public GroupInfoRes createGroup(GroupReq groupReq,
                                    CustomUserDetails userDetails) {
        // 1. 그룹 생성
        Group group = Group.from(groupReq);

        // 1-2. 빈 멤버 목록을 가진 채팅방 생성
        ChatRoomRes roomRes = chatRoomService.createChatRoom(
                CreateRoomReq.of(group.getName(), new ArrayList<>()),
                userDetails);

        // 1-3. 그룹에 chatRoomId 추가 후 저장
        group.updateChatRoomId(roomRes.roomId());
        groupStore.saveGroup(group);

        // 2. 현재 사용자를 Leader로 추가 및 채팅방에 추가
        participantService.createGroupLeader(group.getId(), userDetails);

        // 3. Participant가 있다면 Member 추가 및 채팅방에 추가
        if (groupReq.members() != null && !groupReq.members().isEmpty()) {
            participantService.createParticipants(group.getId(), groupReq.members(),
                    userDetails);
        }

        return GroupInfoRes.from(group);
    }

    @Transactional(readOnly = true)
    public GroupInfoRes getGroup(Long groupId, CustomUserDetails userDetails) {
        participantService.memberAuthorityCheck(groupId, userDetails);
        Group group = groupReader.getGroup(groupId);
        return GroupInfoRes.from(group);
    }

    @Transactional(readOnly = true)
    public GroupsRes getGroups(CustomUserDetails userDetails) {
        List<Group> groups = groupReader.getGroups(userDetails);
        List<Long> groupIds = groups.stream().map(Group::getId).toList();
        List<ParticipantProfileRes> profileDtos = participantReader.getParticipantProfiles(groupIds);
        return GroupsRes.of(groups, groupIds, profileDtos);
    }

    @Transactional
    public GroupInfoRes updateGroup(Long groupId, GroupUpdateReq groupUpdateReq,
                                    CustomUserDetails userDetails) {
        participantService.leaderAuthorityCheck(groupId, userDetails);
        Group group = groupReader.getGroup(groupId);
        group.update(groupUpdateReq);
        return GroupInfoRes.from(group);
    }

    @Transactional
    public void deleteGroup(Long groupId, CustomUserDetails userDetails) {
        participantService.leaderAuthorityCheck(groupId, userDetails);
        Group group = groupReader.getGroup(groupId);
        group.deleteGroup();
    }

    @Transactional(readOnly = true)
    public GroupGatheringRes getGroupGatheringInfo(Long groupId, CustomUserDetails userDetails) {
        participantService.memberAuthorityCheck(groupId, userDetails);
        Group group = groupReader.getGroup(groupId);
        return GroupGatheringRes.from(group);
    }

}
