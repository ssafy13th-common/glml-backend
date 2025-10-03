package com.ssafy.a705.domain.member.dto.response;

import com.ssafy.a705.domain.board._post.entity.Post;
import java.util.List;
import org.springframework.data.domain.Page;

public record MemberPostsRes(
        List<MemberPost> post,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {

    public static MemberPostsRes from(Page<Post> boards) {
        return new MemberPostsRes(boards.stream()
                .map(MemberPost::from).toList(),
                boards.getNumber(),
                boards.getSize(),
                boards.getTotalElements(),
                boards.getTotalPages()
        );
    }

}
