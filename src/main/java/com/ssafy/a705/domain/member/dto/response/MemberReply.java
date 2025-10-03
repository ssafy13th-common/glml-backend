package com.ssafy.a705.domain.member.dto.response;

import com.ssafy.a705.domain.board._reply.entity.Reply;

public record MemberReply(
        Long boardId,
        String boardTitle,
        Long commentId,
        String content
) {

    public static MemberReply from(Reply comment) {
        return new MemberReply(
                comment.getPost().getId(),
                comment.getPost().getTitle(),
                comment.getId(),
                comment.getContent()
        );
    }

}
