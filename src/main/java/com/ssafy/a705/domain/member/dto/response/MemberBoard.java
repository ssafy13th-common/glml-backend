package com.ssafy.a705.domain.member.dto.response;

import com.ssafy.a705.domain.board.entity.CompanyBoard;

public record MemberBoard(
        Long boardId,
        String title,
        String content,
        int comments
) {

    public static MemberBoard from(CompanyBoard board) {
        return new MemberBoard(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getCommentCount()
        );
    }

}
