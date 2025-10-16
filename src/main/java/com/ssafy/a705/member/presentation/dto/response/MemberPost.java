package com.ssafy.a705.member.presentation.dto.response;

import com.ssafy.a705.board.domain.entity.Post;

public record MemberPost(
        Long postId,
        String title,
        String content,
        int replies
) {

    public static MemberPost from(Post board) {
        return new MemberPost(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getReplyCount()
        );
    }

}
