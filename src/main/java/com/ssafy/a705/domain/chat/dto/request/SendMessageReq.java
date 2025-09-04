package com.ssafy.a705.domain.chat.dto.request;

public record SendMessageReq(
        String tempId, // 서버로 전달되기 전에는 messageId가 없기 때문에 ack처리를 위해 임시로 클라이언트에서 할당하는 id
        String roomId, // 채팅방 id
        String content // 채팅 내용
) {

}
