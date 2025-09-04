package com.ssafy.a705.domain.group._livelocation.dto;

import java.time.LocalDateTime;

public record LiveLocationReq(Long groupId, Double latitude, Double longitude,
                              LocalDateTime timestamp) {

}
