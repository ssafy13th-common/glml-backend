package com.ssafy.a705.global.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {

    private static final String MESSAGE = "인증 정보가 없습니다.";

    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
