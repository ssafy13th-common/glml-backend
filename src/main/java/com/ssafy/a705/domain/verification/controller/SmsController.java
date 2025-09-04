package com.ssafy.a705.domain.verification.controller;

import com.ssafy.a705.domain.verification.dto.request.SmsCertificationCodeReq;
import com.ssafy.a705.domain.verification.dto.request.SmsVerifyReq;
import com.ssafy.a705.domain.verification.service.SmsService;
import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/verification/sms")
public class SmsController {

    private final SmsService smsService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> sendVerificationCode(
            @RequestBody @Valid SmsCertificationCodeReq smsReq,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        smsService.sendVerificationCode(smsReq, userDetails);
        return ApiResponse.ok();
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyCode(
            @RequestBody @Valid SmsVerifyReq smsVerifyReq,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        smsService.verifyCode(smsVerifyReq, userDetails);
        return ApiResponse.ok();
    }
}
