package com.ssafy.a705.domain.group._receipt.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class FlaskTimeoutException extends ApiException {

    private static final String MESSAGE = "OCR 분석 중 타임 아웃이 발생했습니다.";

    public FlaskTimeoutException() {
        super(HttpStatus.GATEWAY_TIMEOUT, MESSAGE);
    }
}
