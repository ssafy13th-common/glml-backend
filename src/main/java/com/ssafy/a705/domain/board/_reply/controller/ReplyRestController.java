package com.ssafy.a705.domain.board._reply.controller;

import com.ssafy.a705.domain.board._reply.dto.request.ReplyRegisterReq;
import com.ssafy.a705.domain.board._reply.dto.request.ReplyUpdateReq;
import com.ssafy.a705.domain.board._reply.service.ReplyService;
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
@RequestMapping("/api/v1/posts/{post-id}/replies")
public class ReplyRestController {

    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createReply(
            @PathVariable("post-id") Long postId,
            @RequestBody @Valid ReplyRegisterReq replyReq,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        replyService.createReply(postId, replyReq, userDetails);
        return ApiResponse.create();
    }

    @PutMapping("/{reply-id}")
    public ResponseEntity<ApiResponse<Void>> updateReply(
            @PathVariable("post-id") Long postId,
            @PathVariable("reply-id") Long replyId,
            @RequestBody @Valid ReplyUpdateReq replyUpdateReq,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        replyService.updateReply(postId, replyId, replyUpdateReq, userDetails);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{reply-id}")
    public ResponseEntity<ApiResponse<Void>> deleteReply(
            @PathVariable("post-id") Long postId,
            @PathVariable("reply-id") Long replyId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        replyService.deleteReply(postId, replyId, userDetails);
        return ApiResponse.ok();
    }

}