package com.ssafy.a705.domain.chat.entity;

import com.ssafy.a705.domain.chat.dto.ChatMemberInfoDTO;
import com.ssafy.a705.domain.chat.dto.request.AddMemberReq;
import com.ssafy.a705.domain.chat.dto.request.AddMembersReq;
import com.ssafy.a705.domain.chat.dto.request.CreateRoomReq;
import com.ssafy.a705.domain.chat.dto.request.RemoveMemberReq;
import jakarta.persistence.Id;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Document(collection = "chat_rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    private String id; // room Id
    private String name; // 채팅방 이름
    //    private List<String> participantIds; // 참여자 id 리스트
    private List<ChatMemberInfoDTO> membersInfo;

    private ChatRoom(String name, List<ChatMemberInfoDTO> membersInfo) {
        this.name = name;
        this.membersInfo = membersInfo;
    }

    public static ChatRoom from(CreateRoomReq req) {
        return new ChatRoom(req.name(), req.membersInfo());
    }

    // 멤버 여러 명 추가
    public void addMembers(AddMembersReq request) {
        if (!request.roomId().equals(id)) {
            return;
        }

        membersInfo.addAll(request.memberInfos());
    }

    public void addMember(AddMemberReq request) {
        membersInfo.add(ChatMemberInfoDTO.of(request.memberEmail(), request.memberNickname()));
    }

    //멤버 삭제
    public void removeMember(RemoveMemberReq request) {
        if (!request.roomId().equals(id)) {
            return;
        }

        membersInfo = request.memberInfos();
    }

}
