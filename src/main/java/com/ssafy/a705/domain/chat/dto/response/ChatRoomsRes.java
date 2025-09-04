package com.ssafy.a705.domain.chat.dto.response;

import java.util.List;

public record ChatRoomsRes(
        List<ChatRoomRes> rooms
) {

    public static ChatRoomsRes of(List<ChatRoomRes> rooms) {
        return new ChatRoomsRes(rooms);
    }
}
