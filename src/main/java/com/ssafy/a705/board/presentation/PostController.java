package com.ssafy.a705.board.presentation;

import com.ssafy.a705.board.application.PostApplicationService;
import com.ssafy.a705.board.presentation.dto.request.PostDetailReq;
import com.ssafy.a705.board.presentation.dto.response.PostCreateRes;
import com.ssafy.a705.board.presentation.dto.response.PostDetailRes;
import com.ssafy.a705.board.presentation.dto.response.PostInfosRes;
import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostApplicationService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostCreateRes>> createPost(
            @RequestBody @Valid PostDetailReq postCreateReq,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        PostCreateRes res = postService.createPost(postCreateReq, userDetails);
        return ApiResponse.create(res);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PostInfosRes>> getPosts(
            @RequestParam(required = false) Long cursorId
    ) {
        PostInfosRes res = postService.getPosts(cursorId);
        return ApiResponse.ok(res);
    }

    @GetMapping("/{post-id}")
    public ResponseEntity<ApiResponse<PostDetailRes>> getPost(
            @PathVariable("post-id") Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        PostDetailRes res = postService.getPost(postId, userDetails);
        return ApiResponse.ok(res);
    }

    @PutMapping("/{post-id}")
    public ResponseEntity<ApiResponse<Void>> updatePost(
            @PathVariable("post-id") Long postId,
            @RequestBody @Valid PostDetailReq postReq,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        postService.updatePost(postId, postReq, userDetails);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable("post-id") Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        postService.deletePost(postId, userDetails);
        return ApiResponse.ok();
    }


}