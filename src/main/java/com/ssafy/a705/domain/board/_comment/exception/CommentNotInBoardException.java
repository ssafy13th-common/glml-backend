package com.ssafy.a705.domain.board._comment.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class CommentNotInBoardException extends ApiException {

    private static final String MESSAGE = "요청한 댓글이 해당 게시글에 존재하지 않습니다.";

    public CommentNotInBoardException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}