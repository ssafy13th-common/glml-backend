package com.ssafy.a705.domain.group._member.dto.request;

public record GroupMemberUpdateReq(
        Long groupMemberId,
        int finalAmount,
        int lateFee
) {

}
