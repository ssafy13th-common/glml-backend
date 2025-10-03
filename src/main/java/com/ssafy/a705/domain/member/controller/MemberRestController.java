package com.ssafy.a705.domain.member.controller;

import com.ssafy.a705.domain.member.dto.request.UpdateNicknameReq;
import com.ssafy.a705.domain.member.dto.request.UpdateProfileReq;
import com.ssafy.a705.domain.member.dto.response.MemberPostsRes;
import com.ssafy.a705.domain.member.dto.response.MemberRepliesRes;
import com.ssafy.a705.domain.member.dto.response.MemberDetailRes;
import com.ssafy.a705.domain.member.dto.response.MemberInfosRes;
import com.ssafy.a705.domain.member.service.MemberService;
import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberRestController {

    private final MemberService memberService;

    @GetMapping("/mypage/posts")
    public ResponseEntity<ApiResponse<MemberPostsRes>> getMemberBoards(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault Pageable pageable
    ) {
        MemberPostsRes postsRes = memberService.getMemberBoards(userDetails, pageable);
        return ApiResponse.ok(postsRes);
    }

    @GetMapping("/mypage/replies")
    public ResponseEntity<ApiResponse<MemberRepliesRes>> getMemberReplies(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault Pageable pageable
    ) {
        MemberRepliesRes repliesRes = memberService.getMemberReplies(userDetails, pageable);
        return ApiResponse.ok(repliesRes);
    }

    @GetMapping("/mypage/me")
    public ResponseEntity<ApiResponse<MemberDetailRes>> getMemberDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MemberDetailRes res = memberService.getMemberDetail(userDetails);
        return ApiResponse.ok(res);
    }

    @PatchMapping("/mypage/me/profile")
    public ResponseEntity<ApiResponse<Void>> updateProfile(
            @RequestBody @Valid UpdateProfileReq profileReq,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        memberService.updateProfile(profileReq, userDetails);
        return ApiResponse.ok();
    }

    @PatchMapping("/mypage/me/nickname")
    public ResponseEntity<ApiResponse<Void>> updateNickname(
            @RequestBody @Valid UpdateNicknameReq nicknameReq,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        memberService.updateNickname(nicknameReq, userDetails);
        return ApiResponse.ok();
    }

    @GetMapping("/members")
    public ResponseEntity<ApiResponse<MemberInfosRes>> findMember(
            @RequestParam String search,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MemberInfosRes memberInfosRes = memberService.findMember(search, userDetails);
        return ApiResponse.ok(memberInfosRes);
    }

}
