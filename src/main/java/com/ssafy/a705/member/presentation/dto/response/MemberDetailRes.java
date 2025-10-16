package com.ssafy.a705.member.presentation.dto.response;

import com.ssafy.a705.member.domain.entity.Member;

public record MemberDetailRes(
        String profileUrl,
        String nickname,
        String email
) {

    public static MemberDetailRes from(Member member, String profileUrl) {
        return new MemberDetailRes(profileUrl, member.getNickname(), member.getEmail());
    }

}
