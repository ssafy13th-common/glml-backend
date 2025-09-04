package com.ssafy.a705.domain.chat.dto.request;

import com.ssafy.a705.domain.chat.dto.ChatMemberInfoDTO;
import java.util.List;

public record AddMembersReq(
        String roomId,
        List<ChatMemberInfoDTO> memberInfos
) {

    public static AddMembersReq of(String roomId, List<ChatMemberInfoDTO> memberInfos) {
        return new AddMembersReq(roomId, memberInfos);
    }
}
