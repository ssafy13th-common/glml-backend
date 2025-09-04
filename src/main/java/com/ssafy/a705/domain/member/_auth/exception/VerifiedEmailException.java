package com.ssafy.a705.domain.member._auth.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class VerifiedEmailException extends ApiException {

    private static final String MESSAGE = "이미 인증된 이메일입니다.";

    public VerifiedEmailException() {
        super(HttpStatus.CONFLICT, MESSAGE);
    }
}
