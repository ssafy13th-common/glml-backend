package com.ssafy.a705.board.presentation.dto.response;

import com.ssafy.a705.board.domain.entity.Reply;
import java.time.LocalDate;
import java.util.Objects;

public record ReplyRes(
        Long id,
        String content,
        String author,
        String authorProfileUrl,
        String authorEmail,
        LocalDate updatedDate,
        Long parentReply
) {

    public static ReplyRes from(Reply reply, String url) {
        Reply parent = reply.getParent();
        Long parentId = (Objects.isNull(parent)) ? null : parent.getId();

        return new ReplyRes(
                reply.getId(),
                reply.getContent(),
                reply.getMember().getNickname(),
                url,
                reply.getMember().getEmail(),
                reply.getModifiedAt().toLocalDate(),
                parentId
        );
    }

}