package com.ssafy.a705.domain.chat.dto.response;

import com.ssafy.a705.domain.chat.dto.ChatMemberInfoDTO;
import java.util.List;

public record ChatRoomMembersRes(
        List<ChatMemberInfoDTO> membersList
) {

    public static ChatRoomMembersRes of(List<ChatMemberInfoDTO> membersList) {
        return new ChatRoomMembersRes(membersList);
    }
}
