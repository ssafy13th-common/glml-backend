package com.ssafy.a705.domain.member.dto.response;

import com.ssafy.a705.domain.board._reply.entity.Reply;
import java.util.List;
import org.springframework.data.domain.Page;

public record MemberRepliesRes(
        List<MemberReply> replies,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {

    public static MemberRepliesRes from(Page<Reply> replies) {
        return new MemberRepliesRes(
                replies.stream()
                        .map(MemberReply::from)
                        .toList(),
                replies.getNumber(),
                replies.getSize(),
                replies.getTotalElements(),
                replies.getTotalPages()
        );
    }

}
