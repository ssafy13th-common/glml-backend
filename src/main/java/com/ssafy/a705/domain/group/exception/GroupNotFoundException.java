package com.ssafy.a705.domain.group.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class GroupNotFoundException extends ApiException {

    private static final String MESSAGE = "그룹 정보를 찾을 수 없습니다.";

    public GroupNotFoundException() {
        super(HttpStatus.NOT_FOUND, MESSAGE);
    }
}
