package com.ssafy.a705.domain.verification.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class SmsSendException extends ApiException {

    public SmsSendException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
