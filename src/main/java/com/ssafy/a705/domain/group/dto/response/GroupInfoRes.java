package com.ssafy.a705.domain.group.dto.response;

import com.ssafy.a705.domain.group.entity.Group;
import com.ssafy.a705.domain.group.entity.GroupStatus;
import java.time.LocalDate;

public record GroupInfoRes(
        Long groupId,
        String name,
        String summary,
        String chatRoomId,
        GroupStatus status,
        LocalDate startAt,
        LocalDate endAt,
        int feePerMinute
) {

    public static GroupInfoRes from(Group group) {
        return new GroupInfoRes(group.getId(), group.getName(), group.getSummary(),
                group.getChatRoomId(),
                group.getGroupStatus(),
                group.getStartAt(), group.getEndAt(), group.getFeePerMinute());
    }
}
