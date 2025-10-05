package com.ssafy.a705.board.presentation.dto.response;

import com.ssafy.a705.board.domain.entity.Post;
import java.time.LocalDate;

public record PostInfoRes(
        Long id,
        String title,
        String author,
        String summary,
        LocalDate createdDate,
        int comments
) {

    public static PostInfoRes from(Post post) {
        String summary = post.getContent().length() > 100
                ? post.getContent().substring(0, 100) + "..."
                : post.getContent();

        return new PostInfoRes(post.getId(),
                post.getTitle(),
                post.getMember().getNickname(),
                summary,
                post.getCreatedAt().toLocalDate(),
                post.getReplyCount());
    }

}