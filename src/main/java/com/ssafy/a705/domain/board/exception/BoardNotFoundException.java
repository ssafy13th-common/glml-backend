package com.ssafy.a705.domain.board.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class BoardNotFoundException extends ApiException {

    private static final String MESSAGE = "게시판 정보를 찾을 수 없습니다.";

    public BoardNotFoundException() {
        super(HttpStatus.NOT_FOUND, MESSAGE);
    }
}