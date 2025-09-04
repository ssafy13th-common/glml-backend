package com.ssafy.a705.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ChatMessageWithReadCount(
        String messageId,
        String roomId,
        String senderId,
        String content,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime timestamp,
        int readCount
) {

}
