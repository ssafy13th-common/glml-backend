package com.ssafy.a705.board.domain.service;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.domain.member.entity.Member;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostReader {

    Page<Post> getMemberPost(Member member, Pageable pageable);

    Post getPost(Long postId);

    List<Post> getPosts(Long cursorId, Pageable pageable);
}
