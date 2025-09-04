package com.ssafy.a705.domain.board.dto.response;

import com.ssafy.a705.domain.board._comment.dto.response.CommentRes;
import com.ssafy.a705.domain.board._comment.entity.CompanyComment;
import com.ssafy.a705.domain.board.entity.CompanyBoard;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record BoardDetailRes(
        String title,
        String content,
        String author,
        String authorProfileUrl,
        String authorEmail,
        LocalDate updatedDate,
        List<CommentRes> comments
) {

    public static BoardDetailRes from(CompanyBoard board, List<CompanyComment> comments,
            Map<Long, String> profileUrls) {
        List<CommentRes> commentRes = comments.stream()
                .map(value ->
                        CommentRes.from(value, profileUrls.get(value.getMember().getId())))
                .toList();

        return new BoardDetailRes(
                board.getTitle(),
                board.getContent(),
                board.getMember().getNickname(),
                profileUrls.get(board.getMember().getId()),
                board.getMember().getEmail(),
                board.getModifiedAt().toLocalDate(),
                commentRes
        );
    }

}
