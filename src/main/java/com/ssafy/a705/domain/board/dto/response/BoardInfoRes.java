package com.ssafy.a705.domain.board.dto.response;

import com.ssafy.a705.domain.board.entity.CompanyBoard;
import java.time.LocalDate;

public record BoardInfoRes(
        Long id,
        String title,
        String author,
        String summary,
        LocalDate createdDate,
        int comments
) {

    public static BoardInfoRes from(CompanyBoard board) {
        String summary = board.getContent().length() > 100
                ? board.getContent().substring(0, 100) + "..."
                : board.getContent();
        
        return new BoardInfoRes(board.getId(),
                board.getTitle(),
                board.getMember().getNickname(),
                summary,
                board.getCreatedAt().toLocalDate(),
                board.getCommentCount());
    }

}