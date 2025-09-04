package com.ssafy.a705.domain.group.dto.response;

import com.ssafy.a705.domain.group.entity.Group;
import java.time.LocalDateTime;

public record GroupGatheringRes(
        LocalDateTime gatheringTime,
        String gatheringLocation
) {

    public static GroupGatheringRes from(Group group) {
        return new GroupGatheringRes(group.getGatheringTime(), group.getGatheringLocation());
    }
}
