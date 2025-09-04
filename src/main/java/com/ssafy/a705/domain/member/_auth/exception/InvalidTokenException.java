package com.ssafy.a705.domain.member._auth.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends ApiException {

    private static final String MESSAGE = "만료 되었거나 유효하지 않은 토큰입니다.";

    public InvalidTokenException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
