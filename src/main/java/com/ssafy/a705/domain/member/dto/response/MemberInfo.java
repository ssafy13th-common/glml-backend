package com.ssafy.a705.domain.member.dto.response;

import com.ssafy.a705.domain.member.entity.Member;

public record MemberInfo(
        String email,
        String nickname,
        String profileUrl
) {

    public static MemberInfo from(Member member) {
        return new MemberInfo(member.getEmail(), member.getNickname(), member.getProfileUrl());
    }
}
