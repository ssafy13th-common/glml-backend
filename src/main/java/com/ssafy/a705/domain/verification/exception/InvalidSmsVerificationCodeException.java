package com.ssafy.a705.domain.verification.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidSmsVerificationCodeException extends ApiException {

    private static final String MESSAGE = "인증번호가 일치하지 않습니다.";

    public InvalidSmsVerificationCodeException() {
        super(HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
