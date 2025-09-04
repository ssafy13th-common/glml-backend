package com.ssafy.a705.domain.member._auth.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class DuplicatedMemberException extends ApiException {

    private static final String MESSAGE = "이미 가입 되어있는 이메일입니다.";

    public DuplicatedMemberException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
