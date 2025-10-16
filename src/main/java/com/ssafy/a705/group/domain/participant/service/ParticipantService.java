package com.ssafy.a705.group.domain.participant.service;

import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.group.domain.group.entity.Group;

public interface ParticipantService {

    void createGroupLeader(Group group, Member member);

    void addChatRoom(Group group, Member member);
}
