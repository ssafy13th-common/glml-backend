package com.ssafy.a705.domain.chat.controller;

import com.ssafy.a705.domain.chat.dto.response.ChatHistoryRes;
import com.ssafy.a705.domain.chat.dto.response.ChatRoomMembersRes;
import com.ssafy.a705.domain.chat.service.ChatRoomService;
import com.ssafy.a705.domain.chat.service.ChatService;
import com.ssafy.a705.global.common.controller.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/chat")
public class ChatHistoryController {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/{room-id}/history")
    public ResponseEntity<ApiResponse<ChatHistoryRes>> getChatHistory(
            @PathVariable("room-id") String roomId,
            @PageableDefault(size = 30) Pageable pageable) {
        Pageable pgb = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Direction.DESC, "timestamp"));
        ChatRoomMembersRes membersRes = chatRoomService.getChatRoomMembers(roomId);
        ChatHistoryRes historyRes = chatService.getHistory(roomId, membersRes.membersList(), pgb);
        return ApiResponse.ok(historyRes);
    }
}
