package com.ssafy.a705.group.infrastructure.participant.service;

import com.ssafy.a705.chat.dto.request.AddMemberReq;
import com.ssafy.a705.chat.service.ChatRoomService;
import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.group.domain.group.entity.Group;
import com.ssafy.a705.group.domain.group.service.GroupReader;
import com.ssafy.a705.group.domain.participant.entity.Participant;
import com.ssafy.a705.group.domain.participant.entity.Role;
import com.ssafy.a705.group.domain.participant.exception.DuplicatedParticipantException;
import com.ssafy.a705.group.domain.participant.service.ParticipantReader;
import com.ssafy.a705.group.domain.participant.service.ParticipantService;
import com.ssafy.a705.group.domain.participant.service.ParticipantStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantReader participantReader;
    private final GroupReader groupReader;
    private final ChatRoomService chatRoomService;
    private final ParticipantStore participantStore;

    @Override
    public void createGroupLeader(Group group, Member member) {
        if (participantReader.isExists(group.getId(), member.getId())) {
            throw new DuplicatedParticipantException();
        }
        Participant groupMember = Participant.of(group, member, Role.LEADER);

        participantStore.saveParticipant(groupMember);
    }

    @Override
    public void addChatRoom(Group group, Member member) {
        chatRoomService.addMember(
                AddMemberReq.of(group.getChatRoomId(), member.getEmail(), member.getNickname()));

    }
}
