package com.ssafy.a705.domain.board._post.dto.response;

import com.ssafy.a705.domain.board._post.entity.Post;

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
