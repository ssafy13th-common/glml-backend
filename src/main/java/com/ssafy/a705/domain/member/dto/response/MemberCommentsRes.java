package com.ssafy.a705.domain.member.dto.response;

import com.ssafy.a705.domain.board._comment.entity.CompanyComment;
import java.util.List;
import org.springframework.data.domain.Page;

public record MemberCommentsRes(
        List<MemberComment> comments,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {

    public static MemberCommentsRes from(Page<CompanyComment> comments) {
        return new MemberCommentsRes(
                comments.stream()
                        .map(MemberComment::from)
                        .toList(),
                comments.getNumber(),
                comments.getSize(),
                comments.getTotalElements(),
                comments.getTotalPages()
        );
    }

}
