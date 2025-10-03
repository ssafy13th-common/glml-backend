package com.ssafy.a705.domain.member.dto.response;

import com.ssafy.a705.domain.board._reply.entity.Reply;
import java.util.List;
import org.springframework.data.domain.Page;

public record MemberRepliesRes(
        List<MemberReply> comments,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {

    public static MemberRepliesRes from(Page<Reply> comments) {
        return new MemberRepliesRes(
                comments.stream()
                        .map(MemberReply::from)
                        .toList(),
                comments.getNumber(),
                comments.getSize(),
                comments.getTotalElements(),
                comments.getTotalPages()
        );
    }

}
