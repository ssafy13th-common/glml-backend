package com.ssafy.a705.domain.group._livelocation.dto;

import com.ssafy.a705.domain.group.entity.Group;
import java.time.LocalDateTime;
import java.util.Objects;

public record GroupGatheringDto(LocalDateTime gatheringTime, Double latitude, Double longitude,
                                Integer feePerMinute) {

    public static GroupGatheringDto from(Group group) {
        return new GroupGatheringDto(
                Objects.isNull(group.getGatheringTime()) ? LocalDateTime.now().plusHours(1)
                        : group.getGatheringTime(),
                Objects.isNull(group.getLocationLatitude()) ? 37.55467884
                        : group.getLocationLatitude(),
                Objects.isNull(group.getLocationLongitude()) ? 126.970606
                        : group.getLocationLongitude(), group.getFeePerMinute());
    }
}
