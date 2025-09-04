package com.ssafy.a705.domain.group._member.dto.response;

import com.ssafy.a705.domain.group._member.entity.GroupMember;
import com.ssafy.a705.domain.group._member.entity.Role;

public record GroupMemberInfoRes(
        Long groupMemberId,
        Role role,
        String profileImageUrl,
        String nickname,
        int finalAmount,
        int lateFee
) {

    public static GroupMemberInfoRes from(GroupMember groupMember) {
        return new GroupMemberInfoRes(groupMember.getId(),
                groupMember.getRole(),
                groupMember.getMember().getProfileUrl(),
                groupMember.getMember().getNickname(),
                groupMember.getFinalAmount(),
                groupMember.getLateFee());
    }
}
