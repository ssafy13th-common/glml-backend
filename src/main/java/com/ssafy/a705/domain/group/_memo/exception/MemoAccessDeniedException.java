package com.ssafy.a705.domain.group._memo.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class MemoAccessDeniedException extends ApiException {

    private static final String MESSAGE = "수정 권한이 없는 사용자입니다.";

    public MemoAccessDeniedException() {
        super(HttpStatus.FORBIDDEN, MESSAGE);
    }
}
