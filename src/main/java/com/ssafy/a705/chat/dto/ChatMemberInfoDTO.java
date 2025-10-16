package com.ssafy.a705.chat.dto;

public record ChatMemberInfoDTO(
        String email,
        String nickname
) {

    public static ChatMemberInfoDTO of(String email, String nickname) {
        return new ChatMemberInfoDTO(email, nickname);
    }
}
