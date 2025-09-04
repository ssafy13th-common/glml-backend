package com.ssafy.a705.global.common.exception;

import org.springframework.http.HttpStatus;

public class UnsupportedTypeException extends ApiException {

    private static final String MESSAGE = "지원하지 않는 파일 형식입니다.";

    public UnsupportedTypeException() {
        super(HttpStatus.UNSUPPORTED_MEDIA_TYPE, MESSAGE);
    }
}
