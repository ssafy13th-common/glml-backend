package com.ssafy.a705.domain.member.dto.response;

import com.ssafy.a705.domain.board._comment.entity.CompanyComment;

public record MemberComment(
        Long boardId,
        String boardTitle,
        Long commentId,
        String content
) {

    public static MemberComment from(CompanyComment comment) {
        return new MemberComment(
                comment.getCompanyBoard().getId(),
                comment.getCompanyBoard().getTitle(),
                comment.getId(),
                comment.getContent()
        );
    }

}
