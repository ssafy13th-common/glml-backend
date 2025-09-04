package com.ssafy.a705.domain.chat.controller;

import com.ssafy.a705.domain.chat.dto.ChatMessageEvent;
import com.ssafy.a705.domain.chat.dto.request.ReadLogUpdateReq;
import com.ssafy.a705.domain.chat.dto.request.SendMessageReq;
import com.ssafy.a705.domain.chat.dto.response.ReadStatusUpdateRes;
import com.ssafy.a705.domain.chat.service.ChatService;
import com.ssafy.a705.domain.chat.service.RedisPublisher;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final RedisPublisher redisPublisher;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(SendMessageReq request,
            Principal principal, MessageHeaders headers) {
        log.info(headers.toString());
        ChatMessageEvent event = chatService.sendMessage(request, principal.getName());
        redisPublisher.publish("chat.message", event);
    }

    @MessageMapping("/chat.readMessage")
    public void readMessage(ReadLogUpdateReq request,
            Principal principal) {
        ReadStatusUpdateRes response = chatService.readMessage(request, principal.getName());
        redisPublisher.publish("chat.read-status", response);
    }
}
