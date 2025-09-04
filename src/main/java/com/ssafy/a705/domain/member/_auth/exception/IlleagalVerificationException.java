package com.ssafy.a705.domain.member._auth.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class IlleagalVerificationException extends ApiException {

    private static final String MESSAGE = "인증 메일 요청이 너무 많습니다. 잠시 후 다시 시도해주세요";

    public IlleagalVerificationException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
