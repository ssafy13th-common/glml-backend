package com.ssafy.a705.domain.chat.dto.request;

public record ReadLogUpdateReq(
        String roomId,
        String lastReadMessageId
) {

}
