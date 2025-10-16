package com.ssafy.a705.chat.repository;

import com.ssafy.a705.chat.entity.ChatRoom;
import com.ssafy.a705.chat.exception.MissingRoomException;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

    Optional<ChatRoom> findById(String roomId);

    default ChatRoom getById(String roomId) {
        ChatRoom room = findById(roomId).orElseThrow(MissingRoomException::new);
        return room;
    }
}
