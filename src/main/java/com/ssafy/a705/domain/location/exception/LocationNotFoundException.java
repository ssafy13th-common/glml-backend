package com.ssafy.a705.domain.location.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class LocationNotFoundException extends ApiException {

    private static final String MESSAGE = "지역 정보를 찾을 수 없습니다.";

    public LocationNotFoundException() {
        super(HttpStatus.NOT_FOUND, MESSAGE);
    }
}