package com.ssafy.a705.domain.board.dto.response;

import com.ssafy.a705.domain.board.entity.Post;

public record BoardCreateRes(
        Long id,
        String title,
        String content,
        String author,
        String authorProfile,
        String authorEmail
) {

    public static BoardCreateRes from(Post board) {
        return new BoardCreateRes(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getMember().getNickname(),
                board.getMember().getProfileUrl(),
                board.getMember().getEmail()
        );
    }

}
