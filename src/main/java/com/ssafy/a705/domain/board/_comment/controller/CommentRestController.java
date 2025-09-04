package com.ssafy.a705.domain.board._comment.controller;

import com.ssafy.a705.domain.board._comment.dto.request.CommentRegisterReq;
import com.ssafy.a705.domain.board._comment.dto.request.CommentUpdateReq;
import com.ssafy.a705.domain.board._comment.service.CommentService;
import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/boards/{board-id}/comments")
public class CommentRestController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createComment(
            @PathVariable("board-id") Long boardId,
            @RequestBody @Valid CommentRegisterReq commentReq,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        commentService.createComment(boardId, commentReq, userDetails);
        return ApiResponse.create();
    }

    @PutMapping("/{comment-id}")
    public ResponseEntity<ApiResponse<Void>> updateComment(
            @PathVariable("board-id") Long boardId,
            @PathVariable("comment-id") Long commentId,
            @RequestBody @Valid CommentUpdateReq commentUpdateReq,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        commentService.updateComment(boardId, commentId, commentUpdateReq, userDetails);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable("board-id") Long boardId,
            @PathVariable("comment-id") Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        commentService.deleteComment(boardId, commentId, userDetails);
        return ApiResponse.ok();
    }

}