package com.ssafy.a705.domain.location._color.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class HexCodeNotInvalidException extends ApiException {

    private static final String MESSAGE = "HEX 형식이 아닙니다.";

    public HexCodeNotInvalidException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
