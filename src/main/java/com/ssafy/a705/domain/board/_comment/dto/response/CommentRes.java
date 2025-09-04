package com.ssafy.a705.domain.board._comment.dto.response;

import com.ssafy.a705.domain.board._comment.entity.CompanyComment;
import java.time.LocalDate;
import java.util.Objects;

public record CommentRes(
        Long id,
        String content,
        String author,
        String authorProfileUrl,
        String authorEmail,
        LocalDate updatedDate,
        Long parentComment
) {

    public static CommentRes from(CompanyComment comment, String url) {
        CompanyComment parent = comment.getParent();
        Long parentId = (Objects.isNull(parent)) ? null : parent.getId();

        return new CommentRes(
                comment.getId(),
                comment.getContent(),
                comment.getMember().getNickname(),
                url,
                comment.getMember().getEmail(),
                comment.getModifiedAt().toLocalDate(),
                parentId
        );
    }

}