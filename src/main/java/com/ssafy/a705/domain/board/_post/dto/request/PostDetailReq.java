package com.ssafy.a705.domain.board._post.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PostDetailReq(
        @NotBlank(message = "제목이 입력되지 않았습니다.")
        String title,

        @NotBlank(message = "본문이 입력되지 않았습니다.")
        String content
) {

}