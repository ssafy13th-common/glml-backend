package com.ssafy.a705.domain.group._livelocation.Exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class DeserializationException extends ApiException {

    private static final String MESSAGE = "위치 정보 역직렬화 실패";

    public DeserializationException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE);
    }
}
