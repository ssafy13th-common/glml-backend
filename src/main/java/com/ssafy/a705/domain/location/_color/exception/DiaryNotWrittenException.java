package com.ssafy.a705.domain.location._color.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class DiaryNotWrittenException extends ApiException {

    private static final String MESSAGE = "작성된 다이어리가 없습니다.";

    public DiaryNotWrittenException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
