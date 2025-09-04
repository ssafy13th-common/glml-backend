package com.ssafy.a705.domain.group._receipt.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class FlaskApiException extends ApiException {

    private static final String MESSAGE = "OCR 서버와 통신 중 에러가 발생했습니다.";

    public FlaskApiException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE);
    }
}
