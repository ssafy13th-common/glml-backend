package com.ssafy.a705.domain.member.dto.response;

import com.ssafy.a705.board.domain.entity.Reply;

public record MemberReply(
        Long postId,
        String postTitle,
        Long replyId,
        String content
) {

    public static MemberReply from(Reply reply) {
        return new MemberReply(
                reply.getPost().getId(),
                reply.getPost().getTitle(),
                reply.getId(),
                reply.getContent()
        );
    }

}
