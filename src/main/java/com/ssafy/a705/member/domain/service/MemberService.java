package com.ssafy.a705.member.domain.service;

import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.member.domain.entity.Member;

public interface MemberService {

    Member getMember(String email);

    void checkMember(CustomUserDetails userDetails, String email);

}
