package com.ssafy.a705.domain.tracking.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class TrackingNotFoundException extends ApiException {

    private static final String MESSAGE = "트래킹 정보를 찾을 수 없습니다.";

    public TrackingNotFoundException() {
        super(HttpStatus.NOT_FOUND, MESSAGE);
    }
}
