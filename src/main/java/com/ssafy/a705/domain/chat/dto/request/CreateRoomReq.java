package com.ssafy.a705.domain.chat.dto.request;

import com.ssafy.a705.domain.chat.dto.ChatMemberInfoDTO;
import java.util.List;

public record CreateRoomReq(
        String name, // 채팅방 이름
        List<ChatMemberInfoDTO> membersInfo // 참여자 id 리스트
) {

    public static CreateRoomReq of(String name, List<ChatMemberInfoDTO> membersInfo) {
        return new CreateRoomReq(name, membersInfo);
    }
}
