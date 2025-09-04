package com.ssafy.a705.global.security.jwt.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends ApiException {

    private static final String MESSAGE = "유효하지 않은 토큰입니다.";

    public InvalidTokenException() {
        super(HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
