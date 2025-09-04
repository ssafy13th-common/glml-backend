package com.ssafy.a705.domain.group._livelocation.Exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class SessionCloseFailException extends ApiException {

    private static final String MESSAGE = "세션 종료 중 오류 발생";

    public SessionCloseFailException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE);
    }
}
