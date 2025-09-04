package com.ssafy.a705.domain.group._receipt.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class FlaskException extends ApiException {

    private static final String MESSAGE = "OCR 서버에 에러가 발생했습니다.";

    public FlaskException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE);
    }
}
