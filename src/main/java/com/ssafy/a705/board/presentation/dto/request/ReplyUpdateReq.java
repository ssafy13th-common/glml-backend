package com.ssafy.a705.board.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReplyUpdateReq(
        @NotBlank(message = "본문이 입력되지 않았습니다.")
        String content
) {

}