package com.ssafy.a705.domain.member.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class DuplicatedEmailException extends ApiException {

    private static final String MESSAGE = "이미 존재하는 이메일입니다.";

    public DuplicatedEmailException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}