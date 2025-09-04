package com.ssafy.a705.domain.verification.service;

import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.domain.verification.dto.request.SmsCertificationCodeReq;
import com.ssafy.a705.domain.verification.dto.request.SmsVerifyReq;
import com.ssafy.a705.domain.verification.exception.InvalidSmsVerificationCodeException;
import com.ssafy.a705.domain.verification.repository.SmsRepository;
import com.ssafy.a705.domain.verification.util.SmsCertificationUtil;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final SmsRepository smsRepository;
    private final MemberRepository memberRepository;
    private final SmsCertificationUtil smsCertificationUtil;

    private static final SecureRandom random = new SecureRandom();

    @Transactional
    public void sendVerificationCode(SmsCertificationCodeReq smsReq,
            CustomUserDetails userDetails) {
        String phoneNumber = smsReq.phoneNumber();

        Member member = memberRepository.getById(userDetails.getId());

        String certificationCode = String.valueOf(randomNumber());
        smsCertificationUtil.sendSMS(phoneNumber, certificationCode);

        smsRepository.createSmsCertification(phoneNumber, certificationCode);
    }

    @Transactional
    public void verifyCode(SmsVerifyReq smsVerifyReq, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        if (isVerified(smsVerifyReq.phoneNumber(), smsVerifyReq.certificationCode())) {
            smsRepository.deleteSmsCertification(smsVerifyReq.phoneNumber());
            return;
        }
        throw new InvalidSmsVerificationCodeException();
    }

    private int randomNumber() {
        return random.nextInt(900000) + 100000;
    }

    private boolean isVerified(String phoneNumber, String certificationCode) {
        return smsRepository.getSmsCertification(phoneNumber).equals(certificationCode)
                && smsRepository.hasKey(phoneNumber);
    }

}
