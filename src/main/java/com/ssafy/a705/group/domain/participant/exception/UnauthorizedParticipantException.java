package com.ssafy.a705.group.domain.participant.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UnauthorizedParticipantException extends ApiException {

    public static final String MESSAGE = "그룹 수정 권한이 없습니다.";

    public UnauthorizedParticipantException() {
        super(HttpStatus.FORBIDDEN, MESSAGE);
    }
}
