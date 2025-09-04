package com.ssafy.a705.domain.diary.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class DiaryNotFoundException extends ApiException {

    private static final String message = "여행 기록 정보를 찾을 수 없습니다.";

    public DiaryNotFoundException() {
        super(HttpStatus.NOT_FOUND, message);
    }
}
