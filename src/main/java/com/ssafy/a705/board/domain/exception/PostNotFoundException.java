package com.ssafy.a705.board.domain.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class PostNotFoundException extends ApiException {

    private static final String MESSAGE = "게시글 정보를 찾을 수 없습니다.";

    public PostNotFoundException() {
        super(HttpStatus.NOT_FOUND, MESSAGE);
    }
}