package com.ssafy.a705.global.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ApiException {

    private static final String MESSAGE = " 권한이 없습니다.";

    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message + MESSAGE);
    }
}
