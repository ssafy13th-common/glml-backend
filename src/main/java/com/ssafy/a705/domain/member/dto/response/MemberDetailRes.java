package com.ssafy.a705.domain.member.dto.response;

import com.ssafy.a705.domain.member.entity.Member;

public record MemberDetailRes(
        String profileUrl,
        String nickname,
        String email
) {

    public static MemberDetailRes from(Member member, String profileUrl) {
        return new MemberDetailRes(profileUrl, member.getNickname(), member.getEmail());
    }

}
