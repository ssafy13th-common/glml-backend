package com.ssafy.a705.domain.chat.repository;

import com.ssafy.a705.domain.chat.dto.ChatMessageWithReadCount;
import com.ssafy.a705.domain.chat.dto.request.ReadLogUpdateReq;
import java.util.List;

public interface ReadLogCustomRepository {

    void updateLastReadMessage(String roomId, String senderId, String lastReadMessageId);

    int countUsersWhoReadMessage(String roomId, String messageId);

    List<ChatMessageWithReadCount> countUsersWhoReadMessages(String roomId, int page, int size);

    long countMessages(String roomId);

    void updateAndInsert(ReadLogUpdateReq request, String memberId);
}
