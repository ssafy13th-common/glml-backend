package com.ssafy.a705.global.security.jwt.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class MissingEmailException extends ApiException {

    private static final String MESSAGE = "유저 이메일이 없습니다.";

    public MissingEmailException() {
        super(HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
