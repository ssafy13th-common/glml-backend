package com.ssafy.a705.domain.verification.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class TooManySmsRequestsException extends ApiException {

    private static final String MESSAGE = "요청이 너무 많습니다. 1시간 뒤에 다시 시도해주세요";

    public TooManySmsRequestsException() {
        super(HttpStatus.TOO_MANY_REQUESTS, MESSAGE);
    }
}
