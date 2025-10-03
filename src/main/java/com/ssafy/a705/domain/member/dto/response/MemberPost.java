package com.ssafy.a705.domain.member.dto.response;

import com.ssafy.a705.domain.board._post.entity.Post;

public record MemberPost(
        Long boardId,
        String title,
        String content,
        int comments
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
