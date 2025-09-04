package com.ssafy.a705.domain.verification.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SmsVerifyReq(
        @NotBlank(message = "휴대폰 번호를 입력해주세요.")
        String phoneNumber,

        @NotBlank(message = "인증번호를 입력해주세요.")
        String certificationCode
) {

}
