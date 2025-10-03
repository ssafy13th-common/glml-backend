package com.ssafy.a705.domain.member.dto.response;

import com.ssafy.a705.domain.board._post.entity.Post;

public record MemberBoard(
        Long boardId,
        String title,
        String content,
        int comments
) {

    public static MemberBoard from(Post board) {
        return new MemberBoard(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getReplyCount()
        );
    }

}
