package com.ssafy.a705.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileReq(
        @NotBlank(message = "이메일이 입력되지 않았습니다.")
        String email,

        @NotBlank(message = "프로필 사진이 입력되지 않았습니다.")
        String profileUrl
) {

}
