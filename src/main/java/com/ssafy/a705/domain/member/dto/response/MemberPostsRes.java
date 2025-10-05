package com.ssafy.a705.domain.member.dto.response;

import com.ssafy.a705.board.domain.entity.Post;
import java.util.List;
import org.springframework.data.domain.Page;

public record MemberPostsRes(
        List<MemberPost> post,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {

    public static MemberPostsRes from(Page<Post> posts) {
        return new MemberPostsRes(posts.stream()
                .map(MemberPost::from).toList(),
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages()
        );
    }

}
