package com.ssafy.a705.domain.chat.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class MissingRoomException extends ApiException {

    private static final String MESSAGE = "해당하는 방이 없습니다.";

    public MissingRoomException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
