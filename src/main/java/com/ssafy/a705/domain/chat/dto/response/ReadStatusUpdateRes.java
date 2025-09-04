package com.ssafy.a705.domain.chat.dto.response;

public record ReadStatusUpdateRes(
        String roomId,
        String messageId,
        int readCount
) {

    public static ReadStatusUpdateRes of(String roomId, String messageId, int readCount) {
        return new ReadStatusUpdateRes(roomId, messageId, readCount);
    }
}
