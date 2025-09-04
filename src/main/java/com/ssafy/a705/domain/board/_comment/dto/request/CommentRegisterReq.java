package com.ssafy.a705.domain.board._comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentRegisterReq(
        @NotBlank(message = "본문이 입력되지 않았습니다.")
        String content,

        Long parentId
) {

}