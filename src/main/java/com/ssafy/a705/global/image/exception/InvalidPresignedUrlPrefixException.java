package com.ssafy.a705.global.image.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidPresignedUrlPrefixException extends ApiException {

    private static final String MESSAGE = "유효하지 않은 요청입니다.";

    public InvalidPresignedUrlPrefixException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
