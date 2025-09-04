package com.ssafy.a705.domain.group.dto.request;

import com.ssafy.a705.domain.group.entity.GroupStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record GroupUpdateReq(
        @NotBlank(message = "그룹 이름이 입력되지 않았습니다.")
        String name,
        String summary,
        LocalDateTime gatheringTime,
        String gatheringLocation,
        Double locationLatitude,
        Double locationLongitude,
        LocalDate startAt,
        LocalDate endAt,
        int feePerMinute
) {

}
