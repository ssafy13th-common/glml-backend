package com.ssafy.a705.domain.group.dto.response;

import com.ssafy.a705.domain.group.entity.Group;
import com.ssafy.a705.domain.group.entity.GroupStatus;
import java.util.List;

public record GroupListRes(
        Long groupId,
        String name,
        GroupStatus status,
        String summary,
        List<String> memberProfiles
) {

    public static GroupListRes from(Group group, List<String> imageUrls) {
        return new GroupListRes(group.getId(), group.getName(), group.getGroupStatus(),
                group.getSummary(), imageUrls);
    }
}
