package com.ssafy.a705.domain.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.a705.domain.chat.dto.ChatMessageEvent;
import com.ssafy.a705.domain.chat.dto.response.ReadStatusUpdateRes;
import com.ssafy.a705.domain.chat.entity.ChatMessage;
import com.ssafy.a705.domain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository messageRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String topic = new String(pattern);
            String msgBody = new String(message.getBody());
            log.info("Received message: {}", msgBody);

            if (topic.contains("chat.message")) {
                ChatMessageEvent event = objectMapper.readValue(msgBody,
                        ChatMessageEvent.class);

                ChatMessage saved = messageRepository.save(ChatMessage.from(event));

                ChatMessageEvent newEvent = ChatMessageEvent.from(saved, event.tempId(),
                        event.status());

                String destination = "/topic/chat." + event.roomId();

                messagingTemplate.convertAndSend(destination, newEvent);
            } else if (topic.contains("chat.read-status")) {
                ReadStatusUpdateRes res = objectMapper.readValue(msgBody,
                        ReadStatusUpdateRes.class);
                String destination = "/topic/read-status." + res.roomId();
                messagingTemplate.convertAndSend(destination, res);
            }
        } catch (Exception e) {
            log.error("Error in redis subscriber : {}", e.getMessage());
        }
    }
}
