package com.ssafy.a705.group.presentation.livelocation.dto;

import java.time.LocalDateTime;

public record LiveLocationRes(Long groupId, String memberEmail, Double latitude, Double longitude,
                              LocalDateTime timestamp, Integer lateFee) {

    public static LiveLocationRes from(LiveLocationReq liveLocationReq, String memberEmail,
            Integer lateFee) {
        return new LiveLocationRes(liveLocationReq.groupId(), memberEmail,
                liveLocationReq.latitude(), liveLocationReq.longitude(),
                liveLocationReq.timestamp(), lateFee);
    }
}
