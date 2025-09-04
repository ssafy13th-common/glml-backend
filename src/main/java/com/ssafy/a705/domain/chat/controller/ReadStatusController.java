package com.ssafy.a705.domain.chat.controller;

import com.ssafy.a705.domain.chat.dto.request.ReadLogUpdateReq;
import com.ssafy.a705.domain.chat.dto.response.ReadStatusUpdateRes;
import com.ssafy.a705.domain.chat.service.ChatService;
import com.ssafy.a705.domain.chat.service.RedisPublisher;
import com.ssafy.a705.global.common.controller.ApiResponse;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/chat")
public class ReadStatusController {

    private final ChatService chatService;
    private final RedisPublisher redisPublisher;

    @RequestMapping("/{room-id}/read-status")
    public ResponseEntity<ApiResponse<Void>> updateReadStatus(
            @PathVariable("room-id") String roomId,
            @RequestBody ReadLogUpdateReq request,
            Principal principal
    ) {
        ReadStatusUpdateRes response = chatService.readMessage(request, principal.getName());
        redisPublisher.publish("chat.read-status", response);
        return ApiResponse.ok();
    }
}
