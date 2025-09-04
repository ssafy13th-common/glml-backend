package com.ssafy.a705.domain.chat.dto.request;

import com.ssafy.a705.domain.chat.dto.ChatMemberInfoDTO;
import java.util.List;

public record RemoveMemberReq(
        String roomId,
        List<ChatMemberInfoDTO> memberInfos
) {

    public static RemoveMemberReq of(String roomId, List<ChatMemberInfoDTO> memberInfos) {
        return new RemoveMemberReq(roomId, memberInfos);
    }
}
