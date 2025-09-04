package com.ssafy.a705.domain.member._auth.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class MissingKakaoProfileException extends ApiException {

    private static final String MESSAGE = "카카오 프로필을 가져오지 못했습니다.";

    public MissingKakaoProfileException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
