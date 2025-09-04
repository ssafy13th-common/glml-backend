package com.ssafy.a705.domain.chat.dto.response;

import com.ssafy.a705.domain.chat.entity.ChatAckStatus;

public record ChatAckRes(
        String tempId,
        String messageId,
        ChatAckStatus status
) {

    public static ChatAckRes of(String tempId, String messageId, ChatAckStatus status) {
        return new ChatAckRes(tempId, messageId, status);
    }
}
