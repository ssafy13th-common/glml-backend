package com.ssafy.a705.domain.member.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class SmsUnauthorizedMemberException extends ApiException {

    private static final String MESSAGE = "SMS 인증이 안 된 사용자입니다.";

    public SmsUnauthorizedMemberException() {
        super(HttpStatus.FORBIDDEN, MESSAGE);
    }
}
