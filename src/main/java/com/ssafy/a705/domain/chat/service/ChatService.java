package com.ssafy.a705.domain.chat.service;

import com.ssafy.a705.domain.chat.dto.ChatMemberInfoDTO;
import com.ssafy.a705.domain.chat.dto.ChatMessageEvent;
import com.ssafy.a705.domain.chat.dto.ChatMessageWithReadCount;
import com.ssafy.a705.domain.chat.dto.request.ReadLogUpdateReq;
import com.ssafy.a705.domain.chat.dto.request.SendMessageReq;
import com.ssafy.a705.domain.chat.dto.response.ChatAckRes;
import com.ssafy.a705.domain.chat.dto.response.ChatHistoryRes;
import com.ssafy.a705.domain.chat.dto.response.ReadStatusUpdateRes;
import com.ssafy.a705.domain.chat.entity.ChatAckStatus;
import com.ssafy.a705.domain.chat.entity.ChatMessage;
import com.ssafy.a705.domain.chat.repository.ReadLogRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ReadLogRepository readLogRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MongoTemplate mongoTemplate;

    public ChatMessageEvent sendMessage(SendMessageReq request, String email) {
        String senderId = email;
        ChatMessage message = ChatMessage.from(request, senderId);
        ChatMessageEvent event = ChatMessageEvent.from(message, request.tempId(),
                ChatAckStatus.SENT);
        ChatAckRes res = ChatAckRes.of(request.tempId(), message.getId(), ChatAckStatus.DELIVERED);
        messagingTemplate.convertAndSendToUser(senderId, "queue/ack", res);
        return event;
    }

    public ReadStatusUpdateRes readMessage(ReadLogUpdateReq request, String email) {
        String memberId = email;
        readLogRepository.updateAndInsert(request, email);
        int readCount = readLogRepository.countUsersWhoReadMessage(request.roomId(),
                request.lastReadMessageId());
        return ReadStatusUpdateRes.of(request.roomId(), request.lastReadMessageId(), readCount);
    }

    public ChatHistoryRes getHistory(String roomId, List<ChatMemberInfoDTO> membersList,
            Pageable pageable) {
        List<ChatMessageWithReadCount> messages = readLogRepository.countUsersWhoReadMessages(
                roomId,
                pageable.getPageNumber(), pageable.getPageSize());
        return ChatHistoryRes.of(messages, membersList, pageable,
                readLogRepository.countMessages(roomId));
    }


}
