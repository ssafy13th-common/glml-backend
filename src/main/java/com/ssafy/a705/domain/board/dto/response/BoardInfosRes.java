package com.ssafy.a705.domain.board.dto.response;

import com.ssafy.a705.domain.board.entity.Post;
import java.util.List;

public record BoardInfosRes(
        List<BoardInfoRes> boards,
        Long nextCursor
) {

    public static BoardInfosRes from(List<Post> boards, Long nextCursor) {
        List<BoardInfoRes> boardsRes = boards.stream()
                .map(BoardInfoRes::from)
                .toList();

        return new BoardInfosRes(boardsRes, nextCursor);
    }
}