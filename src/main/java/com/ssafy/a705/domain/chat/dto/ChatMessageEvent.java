package com.ssafy.a705.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.a705.domain.chat.entity.ChatAckStatus;
import com.ssafy.a705.domain.chat.entity.ChatMessage;
import java.time.LocalDateTime;

public record ChatMessageEvent(
        String tempId,
        String roomId,
        String messageId,
        String senderId, // 채팅 송신자 이메일
        String content,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul") LocalDateTime timestamp,
        ChatAckStatus status // SENT, DELIVERED
) {

    public static ChatMessageEvent from(ChatMessage message, String tempId, ChatAckStatus status) {
        return new ChatMessageEvent(tempId, message.getRoomId(), message.getId(),
                message.getSenderId(), message.getContent(), LocalDateTime.now(),
                status);
    }

    public static ChatMessageEvent of(String tempId, String roomId, String messageId,
            String senderId, String content,
            ChatAckStatus status) {
        return new ChatMessageEvent(tempId, roomId, messageId, senderId, content,
                LocalDateTime.now(), status);
    }
}
