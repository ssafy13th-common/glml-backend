package com.ssafy.a705.group.presentation.livelocation.dto;

import java.time.LocalDateTime;

public record LiveLocationReq(Long groupId, Double latitude, Double longitude,
                              LocalDateTime timestamp) {

}
