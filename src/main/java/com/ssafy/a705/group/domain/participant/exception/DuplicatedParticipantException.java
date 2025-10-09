package com.ssafy.a705.group.domain.participant.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class DuplicatedParticipantException extends ApiException {

    private static final String MESSAGE = "이미 그룹에 속한 사용자입니다.";

    public DuplicatedParticipantException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
