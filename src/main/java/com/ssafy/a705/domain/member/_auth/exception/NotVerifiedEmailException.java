package com.ssafy.a705.domain.member._auth.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class NotVerifiedEmailException extends ApiException {

    private static final String MESSAGE = "인증되지 않은 이메일입니다.";

    public NotVerifiedEmailException() {
        super(HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
