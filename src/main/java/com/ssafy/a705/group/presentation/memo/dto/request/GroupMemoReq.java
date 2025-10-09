package com.ssafy.a705.group.presentation.memo.dto.response;

import jakarta.validation.constraints.NotBlank;

public record GroupMemoReq(
        @NotBlank(message = "메모 내용이 입력되지 않았습니다.")
        String content
) {

}
