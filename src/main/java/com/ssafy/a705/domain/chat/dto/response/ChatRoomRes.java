package com.ssafy.a705.domain.chat.dto.response;

import com.ssafy.a705.domain.chat.dto.ChatMemberInfoDTO;
import com.ssafy.a705.domain.chat.entity.ChatRoom;
import java.util.List;

public record ChatRoomRes(
        String roomId,
        String name,
        List<ChatMemberInfoDTO> membersInfo
) {

    public static ChatRoomRes from(ChatRoom room) {
        return new ChatRoomRes(room.getId(), room.getName(),
                room.getMembersInfo());
    }
}
