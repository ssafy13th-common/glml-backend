package com.ssafy.a705.domain.group._member.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class GroupMemberNotFoundException extends ApiException {

    private static final String MESSAGE = "일치하는 그룹 멤버가 없습니다.";

    public GroupMemberNotFoundException() {
        super(HttpStatus.NOT_FOUND, MESSAGE);
    }
}
