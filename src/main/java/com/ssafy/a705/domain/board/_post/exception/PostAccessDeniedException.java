package com.ssafy.a705.domain.board._post.exception;

import com.ssafy.a705.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class PostAccessDeniedException extends ApiException {

    private static final String MESSAGE = "게시글 접근 권한이 없습니다.";

    public PostAccessDeniedException() {
        super(HttpStatus.FORBIDDEN, MESSAGE);
    }
}