package com.ssafy.a705.domain.chat.repository;

import com.ssafy.a705.domain.chat.entity.ChatMessage;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByRoomIdOrderByTimestampAsc(String roomId);

    Page<ChatMessage> findByRoomIdOrderByTimestampDesc(String roomId, Pageable pageable);
}
