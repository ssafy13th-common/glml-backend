package com.ssafy.a705.domain.chat.dto.request;

public record AddMemberReq(
        String roomId,
        String memberEmail,
        String memberNickname
) {

    public static AddMemberReq of(String roomId, String memberEmail, String memberNickname) {
        return new AddMemberReq(roomId, memberEmail, memberNickname);
    }
}
