package com.ssafy.a705.domain.group._member.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UnauthorizedGroupMemberException extends ApiException {

    public static final String MESSAGE = "그룹 수정 권한이 없습니다.";

    public UnauthorizedGroupMemberException() {
        super(HttpStatus.FORBIDDEN, MESSAGE);
    }
}
