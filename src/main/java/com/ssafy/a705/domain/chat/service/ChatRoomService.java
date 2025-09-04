package com.ssafy.a705.domain.chat.service;

import com.ssafy.a705.domain.chat.dto.ChatMemberInfoDTO;
import com.ssafy.a705.domain.chat.dto.request.AddMemberReq;
import com.ssafy.a705.domain.chat.dto.request.AddMembersReq;
import com.ssafy.a705.domain.chat.dto.request.CreateRoomReq;
import com.ssafy.a705.domain.chat.dto.request.RemoveMemberReq;
import com.ssafy.a705.domain.chat.dto.response.ChatRoomMembersRes;
import com.ssafy.a705.domain.chat.dto.response.ChatRoomRes;
import com.ssafy.a705.domain.chat.dto.response.ChatRoomsRes;
import com.ssafy.a705.domain.chat.entity.ChatRoom;
import com.ssafy.a705.domain.chat.exception.MissingRoomException;
import com.ssafy.a705.domain.chat.repository.ChatRoomRepository;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository roomRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MongoTemplate mongoTemplate;

    public ChatRoomRes createChatRoom(CreateRoomReq req, CustomUserDetails customUserDetails) {
        ChatRoom room = ChatRoom.from(req);
        ChatRoom saved = roomRepository.save(room);
        return ChatRoomRes.from(saved);
    }

    public ChatRoomsRes getChatRooms(CustomUserDetails customUserDetails) {
        String userEmail = customUserDetails.getEmail();
        log.info(userEmail);
        // 1대1 채팅만 조회
        List<ChatRoom> chatRooms = mongoTemplate.find(
                new Query(new Criteria().andOperator(
                        Criteria.where("membersInfo")
                                .in(ChatMemberInfoDTO.of(customUserDetails.getEmail(),
                                        customUserDetails.getNickname())),
                        Criteria.where("membersInfo").size(2))), ChatRoom.class);
        if (chatRooms.isEmpty()) {
            throw new MissingRoomException();
        }
        List<ChatRoomRes> resList = chatRooms.stream().map(ChatRoomRes::from).toList();
        return ChatRoomsRes.of(resList);
    }

    public ChatRoomMembersRes getChatRoomMembers(String roomId) {
        ChatRoom room = roomRepository.getById(roomId);

        return ChatRoomMembersRes.of(room.getMembersInfo());
    }

    // 채팅방에 멤버 한 명 추가
    public ChatRoomRes addMember(AddMemberReq request) {
        ChatRoom room = roomRepository.getById(request.roomId());

        room.addMember(request);
        roomRepository.save(room);
        notifyMemberListChanged(room);
        return ChatRoomRes.from(room);
    }

    // 채팅방에 멤버 여러명 추가
    public ChatRoomRes addMembers(AddMembersReq request) {
        ChatRoom room = roomRepository.getById(request.roomId());

        room.addMembers(request);
        roomRepository.save(room);
        notifyMemberListChanged(room);
        return ChatRoomRes.from(room);
    }

    // 채팅방에서 멤버 제거
    public ChatRoomRes removeMember(RemoveMemberReq request) {
        ChatRoom room = roomRepository.getById(request.roomId());

        room.removeMember(request);
        roomRepository.save(room);
        notifyMemberListChanged(room);
        return ChatRoomRes.from(room);
    }

    // 특정 사용자와의 채팅방 조회
    public ChatRoomRes getChatRoom(String email,
            CustomUserDetails customUserDetails) {

        String memberEmail = customUserDetails.getEmail();

        ChatRoom chatRoom = mongoTemplate.findOne(
                new Query(new Criteria().andOperator(
                        Criteria.where("membersInfo").elemMatch(Criteria.where("email").is(email)),
                        Criteria.where("membersInfo")
                                .elemMatch(Criteria.where("email").is(memberEmail)),
                        Criteria.where("membersInfo").size(2))),
                ChatRoom.class);
        if (chatRoom == null) {
            throw new MissingRoomException();
        }

        return ChatRoomRes.from(chatRoom);
    }

    /**
     * 멤버 변경 알림
     *
     * @param room
     */
    private void notifyMemberListChanged(ChatRoom room) {
        messagingTemplate.convertAndSend(
                "/topic/chat/rooms/" + room.getId() + "/members",
                ChatRoomRes.from(room)
        );
    }
}
