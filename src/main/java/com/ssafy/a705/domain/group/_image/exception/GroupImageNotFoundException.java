package com.ssafy.a705.domain.group._image.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class GroupImageNotFoundException extends ApiException {

    private static final String MESSAGE = "사진 정보를 찾을 수 없습니다.";

    public GroupImageNotFoundException() {
        super(HttpStatus.NOT_FOUND, MESSAGE);
    }
}
