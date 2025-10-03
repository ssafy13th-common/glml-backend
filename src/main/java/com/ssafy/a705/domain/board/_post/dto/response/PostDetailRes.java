package com.ssafy.a705.domain.board._post.dto.response;

import com.ssafy.a705.domain.board._reply.dto.response.CommentRes;
import com.ssafy.a705.domain.board._reply.entity.Reply;
import com.ssafy.a705.domain.board._post.entity.Post;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record PostDetailRes(
        String title,
        String content,
        String author,
        String authorProfileUrl,
        String authorEmail,
        LocalDate updatedDate,
        List<CommentRes> comments
) {

    public static PostDetailRes from(Post post, List<Reply> comments,
            Map<Long, String> profileUrls) {
        List<CommentRes> commentRes = comments.stream()
                .map(value ->
                        CommentRes.from(value, profileUrls.get(value.getMember().getId())))
                .toList();

        return new PostDetailRes(
                post.getTitle(),
                post.getContent(),
                post.getMember().getNickname(),
                profileUrls.get(post.getMember().getId()),
                post.getMember().getEmail(),
                post.getModifiedAt().toLocalDate(),
                commentRes
        );
    }

}
