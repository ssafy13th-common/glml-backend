package com.ssafy.a705.domain.board._comment.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class CommentAccessDeniedException extends ApiException {

    private static final String MESSAGE = "댓글 접근 권한이 없습니다.";

    public CommentAccessDeniedException() {
        super(HttpStatus.FORBIDDEN, MESSAGE);
    }
}