package com.ssafy.a705.domain.group._participant.dto.request;

import com.ssafy.a705.domain.group._participant.entity.Role;
import com.ssafy.a705.domain.group.entity.Group;
import com.ssafy.a705.domain.member.entity.Member;

public record ParticipantReq(
        int finalAmount,
        int lateFee,
        Group group,
        Member member,
        Role role
) {


}
