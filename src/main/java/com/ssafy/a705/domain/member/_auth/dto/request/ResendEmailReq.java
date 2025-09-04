package com.ssafy.a705.domain.member._auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ResendEmailReq(
        @NotBlank(message = "이메일이 입력되지 않았습니다.") String email
) {

}
