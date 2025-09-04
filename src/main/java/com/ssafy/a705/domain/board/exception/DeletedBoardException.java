package com.ssafy.a705.domain.board.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class DeletedBoardException extends ApiException {

    private static final String MESSAGE = "삭제된 게시물입니다.";

    public DeletedBoardException() {
        super(HttpStatus.NOT_FOUND, MESSAGE);
    }
}
