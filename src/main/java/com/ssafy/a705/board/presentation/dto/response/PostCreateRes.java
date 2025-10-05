package com.ssafy.a705.board.presentation.dto.response;

import com.ssafy.a705.board.domain.entity.Post;

public record PostCreateRes(
        Long id,
        String title,
        String content,
        String author,
        String authorProfile,
        String authorEmail
) {

    public static PostCreateRes from(Post post) {
        return new PostCreateRes(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getMember().getNickname(),
                post.getMember().getProfileUrl(),
                post.getMember().getEmail()
        );
    }

}
