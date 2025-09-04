package com.ssafy.a705.global.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidFileNameException extends ApiException {

    private static final String MESSAGE = "파일 이름 형식이 잘못되었습니다.";

    public InvalidFileNameException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
