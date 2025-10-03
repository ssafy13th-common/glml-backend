package com.ssafy.a705.domain.board._post.dto.response;

import com.ssafy.a705.domain.board._post.entity.Post;
import java.util.List;

public record PostInfosRes(
        List<PostInfoRes> posts,
        Long nextCursor
) {

    public static PostInfosRes from(List<Post> posts, Long nextCursor) {
        List<PostInfoRes> postsRes = posts.stream()
                .map(PostInfoRes::from)
                .toList();

        return new PostInfosRes(postsRes, nextCursor);
    }
}