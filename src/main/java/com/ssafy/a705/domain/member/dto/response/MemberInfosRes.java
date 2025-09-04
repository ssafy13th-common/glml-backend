package com.ssafy.a705.domain.member.dto.response;

import com.ssafy.a705.domain.member.entity.Member;
import java.util.List;

public record MemberInfosRes(
        List<MemberInfo> members
) {

    public static MemberInfosRes of(List<Member> members) {
        return new MemberInfosRes(
                members.stream()
                        .map(MemberInfo::from)
                        .toList()
        );
    }

}
