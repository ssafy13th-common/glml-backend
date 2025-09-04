package com.ssafy.a705.domain.group._member.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class DuplicatedGroupMemberException extends ApiException {

    private static final String MESSAGE = "이미 그룹에 속한 사용자입니다.";

    public DuplicatedGroupMemberException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
