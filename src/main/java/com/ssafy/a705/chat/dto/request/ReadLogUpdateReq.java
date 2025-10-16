package com.ssafy.a705.chat.dto.request;

public record ReadLogUpdateReq(
        String roomId,
        String lastReadMessageId
) {

}
