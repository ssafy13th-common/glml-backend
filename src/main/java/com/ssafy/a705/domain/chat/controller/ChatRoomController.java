package com.ssafy.a705.domain.chat.controller;

import com.ssafy.a705.domain.chat.dto.request.CreateRoomReq;
import com.ssafy.a705.domain.chat.dto.response.ChatRoomRes;
import com.ssafy.a705.domain.chat.dto.response.ChatRoomsRes;
import com.ssafy.a705.domain.chat.service.ChatRoomService;
import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat/rooms")
public class ChatRoomController {

    private final ChatRoomService roomService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChatRoomRes>> createChatRoom(
            @RequestBody CreateRoomReq req,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        ChatRoomRes res = roomService.createChatRoom(req, customUserDetails);
        return ApiResponse.ok(res);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ChatRoomsRes>> getChatRooms(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ApiResponse.ok(roomService.getChatRooms(customUserDetails));
    }

    @GetMapping("/private")
    public ResponseEntity<ApiResponse<ChatRoomRes>> getChatRoom(
            @RequestParam("email") String email,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ApiResponse.ok(roomService.getChatRoom(email, customUserDetails));
    }
}

