package com.ssafy.a705.domain.member._auth.controller;

import com.ssafy.a705.domain.member._auth.dto.request.KakaoTokenReq;
import com.ssafy.a705.domain.member._auth.dto.request.ResendEmailReq;
import com.ssafy.a705.domain.member._auth.dto.request.SignUpReq;
import com.ssafy.a705.domain.member._auth.service.KakaoService;
import com.ssafy.a705.domain.member._auth.service.MemberAuthService;
import com.ssafy.a705.domain.member.dto.TokenRes;
import com.ssafy.a705.domain.member.service.MemberService;
import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class MemberAuthController {

    private final MemberAuthService authService;
    private final KakaoService kakaoService;
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signUp(@RequestBody @Valid SignUpReq request,
            HttpServletRequest http) {
        String base = "https://i13a705.p.ssafy.io/auth/verify?token=";
        authService.signUp(request, base);
        return ApiResponse.create();
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verify(@RequestParam("token") String token) {
        authService.verify(token);
        return ApiResponse.okMessage("이메일 인증 완료");
    }

    @PostMapping("/verify/resend")
    public ResponseEntity<ApiResponse<Void>> resendVerify(@RequestBody @Valid ResendEmailReq req,
            HttpServletRequest http) {
        String base = "https://i13a705.p.ssafy.io/auth/verify?token=";
        authService.resendVerification(req.email(), base);
        return ApiResponse.ok();
    }

    @PostMapping("/kakao/mobile")
    public ResponseEntity<ApiResponse<TokenRes>> exchangeToken(
            @RequestBody @Valid KakaoTokenReq request) {
        TokenRes response = kakaoService.exchangeTokens(request);
        return ApiResponse.ok(response);
    }

    @PutMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdrawMember(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            HttpServletRequest http) {
        // DB에서 삭제 및 토큰 삭제
        authService.delete(customUserDetails, http);

        return ApiResponse.ok();
    }

    @GetMapping("/email")
    public ResponseEntity<ApiResponse<Void>> checkEmail(
            @RequestParam("email") String email
    ) {
        memberService.checkEmail(email);
        return ApiResponse.okMessage("사용할 수 있는 이메일입니다.");
    }
}
