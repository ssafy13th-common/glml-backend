package com.ssafy.a705.domain.group._livelocation.Exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class SerializationException extends ApiException {

    private static final String MESSAGE = "위치 정보 직렬화 실패";

    public SerializationException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE);
    }
}
