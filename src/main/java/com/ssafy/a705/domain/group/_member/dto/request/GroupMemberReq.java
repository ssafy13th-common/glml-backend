package com.ssafy.a705.domain.group._member.dto.request;

import com.ssafy.a705.domain.group._member.entity.Role;
import com.ssafy.a705.domain.group.entity.Group;
import com.ssafy.a705.domain.member.entity.Member;

public record GroupMemberReq(
        int finalAmount,
        int lateFee,
        Group group,
        Member member,
        Role role
) {


}
