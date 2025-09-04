package com.ssafy.a705.domain.verification.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class AlreadySmsVerifiedMemberException extends ApiException {

    private static final String MESSAGE = "이미 SMS 인증이 완료된 회원입니다.";

    public AlreadySmsVerifiedMemberException() {
        super(HttpStatus.CONFLICT, MESSAGE);
    }
}
