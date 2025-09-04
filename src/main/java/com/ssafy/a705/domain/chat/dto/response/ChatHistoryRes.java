package com.ssafy.a705.domain.chat.dto.response;

import com.ssafy.a705.domain.chat.dto.ChatMemberInfoDTO;
import com.ssafy.a705.domain.chat.dto.ChatMessageWithReadCount;
import java.util.List;
import org.springframework.data.domain.Pageable;

public record ChatHistoryRes(
        List<ChatMessageWithReadCount> messages,
        List<ChatMemberInfoDTO> membersList,
        int page,
        int size,
        long total
) {

    public static ChatHistoryRes of(List<ChatMessageWithReadCount> messages,
            List<ChatMemberInfoDTO> membersList, Pageable pageable,
            long total) {
        return new ChatHistoryRes(messages, membersList, pageable.getPageNumber(),
                pageable.getPageSize(),
                total);
    }
}
