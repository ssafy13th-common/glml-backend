package com.ssafy.a705.domain.chat.entity;

import com.ssafy.a705.domain.chat.dto.ChatMessageEvent;
import com.ssafy.a705.domain.chat.dto.request.SendMessageReq;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "chat_messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    @Comment("채팅 식별자")
    private String id;

    @Comment("채팅방 식별자")
    private String roomId;

    @Comment("채팅 송신자 이메일")
    private String senderId;

    @Comment("채팅 내용")
    private String content;

    @Comment("채팅 송신 시간")
    private LocalDateTime timestamp;

    private ChatMessage(String roomId, String senderId, String content) {
        this.id = UUID.randomUUID().toString();
        this.roomId = roomId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public static ChatMessage from(SendMessageReq request, String senderId) {
        return new ChatMessage(request.roomId(), senderId, request.content());
    }

    public static ChatMessage from(ChatMessageEvent event) {
        return new ChatMessage(event.roomId(), event.senderId(), event.content());
    }
}
