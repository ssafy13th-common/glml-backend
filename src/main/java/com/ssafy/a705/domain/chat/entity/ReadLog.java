package com.ssafy.a705.domain.chat.entity;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "read_logs")
@Getter
@NoArgsConstructor
public class ReadLog {

    @Id
    private String id;
    private String roomId;
    private String memberId; // 유저 email
    private String lastReadMessageId; // 마지막으로 읽은 메세지
    private LocalDateTime updatedAt;
    private LocalDateTime lastReadTimestamp;

    private ReadLog(String roomId, String memberId, String lastReadMessageId,
            LocalDateTime updatedAt, LocalDateTime lastReadTimestamp) {
        this.roomId = roomId;
        this.memberId = memberId;
        this.lastReadMessageId = lastReadMessageId;
        this.updatedAt = updatedAt;
        this.lastReadTimestamp = lastReadTimestamp;
    }

    public static ReadLog of(String roomId, String memberId, String lastReadMessageId,
            LocalDateTime updatedAt, LocalDateTime lastReadTimestamp) {
        return new ReadLog(roomId, memberId, lastReadMessageId, updatedAt, lastReadTimestamp);
    }
}
