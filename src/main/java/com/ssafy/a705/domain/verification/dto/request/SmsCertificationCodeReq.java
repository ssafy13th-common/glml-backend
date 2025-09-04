package com.ssafy.a705.domain.verification.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SmsCertificationCodeReq(
        @NotBlank(message = "휴대폰 번호가 입력되지 않았습니다.")
        String phoneNumber
) {

}