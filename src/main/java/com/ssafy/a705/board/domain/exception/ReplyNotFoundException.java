package com.ssafy.a705.board.domain.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ReplyNotFoundException extends ApiException {

    private static final String MESSAGE = "댓글 정보를 찾을 수 없습니다.";

    public ReplyNotFoundException() {
        super(HttpStatus.NOT_FOUND, MESSAGE);
    }
}