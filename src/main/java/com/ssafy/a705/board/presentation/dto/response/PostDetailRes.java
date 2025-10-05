package com.ssafy.a705.board.presentation.dto.response;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.board.domain.entity.Reply;
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
        List<ReplyRes> repliesRes
) {

    public static PostDetailRes from(Post post, List<Reply> replies,
            Map<Long, String> profileUrls) {
        List<ReplyRes> replyRes = replies.stream()
                .map(value ->
                        ReplyRes.from(value, profileUrls.get(value.getMember().getId())))
                .toList();

        return new PostDetailRes(
                post.getTitle(),
                post.getContent(),
                post.getMember().getNickname(),
                profileUrls.get(post.getMember().getId()),
                post.getMember().getEmail(),
                post.getModifiedAt().toLocalDate(),
                replyRes
        );
    }

}
