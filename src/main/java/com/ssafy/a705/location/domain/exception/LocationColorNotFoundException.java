package com.ssafy.a705.location.domain.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class LocationColorNotFoundException extends ApiException {

    private static final String MESSAGE = "지역 색 정보를 찾을 수 없습니다.";

    public LocationColorNotFoundException() {
        super(HttpStatus.NOT_FOUND, MESSAGE);
    }
}