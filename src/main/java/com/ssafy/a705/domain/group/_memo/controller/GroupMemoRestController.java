package com.ssafy.a705.domain.group._memo.controller;

import com.ssafy.a705.domain.group._memo.dto.request.GroupMemoReq;
import com.ssafy.a705.domain.group._memo.dto.response.GroupMemoRes;
import com.ssafy.a705.domain.group._memo.dto.response.GroupMemosRes;
import com.ssafy.a705.domain.group._memo.service.GroupMemoService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups/{group-id}/memos")
public class GroupMemoRestController {

    private final GroupMemoService groupMemoService;

    @GetMapping
    public ResponseEntity<ApiResponse<GroupMemosRes>> getMemos(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        GroupMemosRes memoResponses = groupMemoService.getMemos(groupId, customUserDetails);
        return ApiResponse.ok(memoResponses);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GroupMemoRes>> createMemo(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid GroupMemoReq request
    ) {
        GroupMemoRes groupMemoRes = groupMemoService.createMemo(groupId, request,
                customUserDetails);
        return ApiResponse.create(groupMemoRes);
    }

    @PutMapping("/{memo-id}")
    public ResponseEntity<ApiResponse<GroupMemoRes>> updateMemo(
            @PathVariable("group-id") Long groupId,
            @PathVariable("memo-id") Long memoId,
            @RequestBody @Valid GroupMemoReq request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        GroupMemoRes groupMemoRes = groupMemoService.updateMemo(groupId, memoId,
                request, customUserDetails);
        return ApiResponse.ok(groupMemoRes);
    }

    @DeleteMapping("/{memo-id}")
    public ResponseEntity<ApiResponse<Void>> deleteMemo(
            @PathVariable("group-id") Long groupId,
            @PathVariable("memo-id") Long memoId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        groupMemoService.deleteMemo(groupId, memoId, customUserDetails);
        return ApiResponse.ok();
    }
}
