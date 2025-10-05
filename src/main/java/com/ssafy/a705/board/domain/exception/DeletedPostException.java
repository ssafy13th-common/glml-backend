package com.ssafy.a705.board.domain.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class DeletedPostException extends ApiException {

    private static final String MESSAGE = "삭제된 게시글입니다.";

    public DeletedPostException() {
        super(HttpStatus.NOT_FOUND, MESSAGE);
    }
}
