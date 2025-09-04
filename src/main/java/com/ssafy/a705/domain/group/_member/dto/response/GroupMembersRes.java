package com.ssafy.a705.domain.group._member.dto.response;

import com.ssafy.a705.domain.group._member.entity.GroupMember;
import java.util.List;

public record GroupMembersRes(
        Long groupId,
        int membersCount,
        List<GroupMemberInfoRes> groupMembers
) {

    public static GroupMembersRes of(Long groupId, List<GroupMember> members) {
        List<GroupMemberInfoRes> groupMembers = members.stream().map(GroupMemberInfoRes::from)
                .toList();
        return new GroupMembersRes(groupId, groupMembers.size(), groupMembers);
    }
}
