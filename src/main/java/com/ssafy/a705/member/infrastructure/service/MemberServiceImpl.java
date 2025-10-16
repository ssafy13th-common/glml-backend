package com.ssafy.a705.member.infrastructure.service;

import com.ssafy.a705.global.common.exception.ForbiddenException;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.member.domain.service.MemberReader;
import com.ssafy.a705.member.domain.service.MemberService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberReader memberReader;

    @Override
    public Member getMember(String email) {
        return memberReader.getMember(email);
    }

    @Override
    public void checkMember(CustomUserDetails userDetails, String email) {
        if (Objects.equals(userDetails.getEmail(), email)) {
            return;
        }
        throw new ForbiddenException("회원 정보 접근");
    }
}
