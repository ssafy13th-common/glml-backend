package com.ssafy.a705.domain.location.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class FileIOException extends ApiException {

    private static final String MESSAGE = "파일 처리 중 오류가 발생했습니다.";

    public FileIOException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE);
    }
}
