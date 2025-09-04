package com.ssafy.a705.domain.diary.dto.request;

import com.ssafy.a705.global.common.validation.annotation.DateOrder;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@DateOrder(start = "startedAt", end = "endedAt")
public record DiaryUpdateReq(
        @NotNull(message = "여행 시작 날짜가 입력되지 않았습니다.")
        LocalDate startedAt,
        @NotNull(message = "여행 종료 날짜가 입력되지 않았습니다.")
        LocalDate endedAt,
        String content,
        List<String> keepImageUrls,
        List<String> newImageUrls
) {

}