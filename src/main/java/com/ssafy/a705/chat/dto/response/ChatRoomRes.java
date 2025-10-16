package com.ssafy.a705.chat.dto.response;

import com.ssafy.a705.chat.dto.ChatMemberInfoDTO;
import com.ssafy.a705.chat.entity.ChatRoom;
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
