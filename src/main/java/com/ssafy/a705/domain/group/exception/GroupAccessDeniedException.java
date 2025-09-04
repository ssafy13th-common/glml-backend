package com.ssafy.a705.domain.group.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class GroupAccessDeniedException extends ApiException {

    private static final String MESSAGE = "그룹 접근 권한이 없습니다.";

    public GroupAccessDeniedException() {
        super(HttpStatus.FORBIDDEN, MESSAGE);
    }
}
