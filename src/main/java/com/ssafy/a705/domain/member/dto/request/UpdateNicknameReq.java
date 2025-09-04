package com.ssafy.a705.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateNicknameReq(
        @NotBlank(message = "이메일이 입력되지 않았습니다.")
        String email,

        @NotBlank(message = "닉네임이 입력되지 않았습니다.")
        String nickname
) {

}
