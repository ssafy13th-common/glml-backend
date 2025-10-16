package com.ssafy.a705.board.domain.repository;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.member.domain.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long id);

    List<Post> findAllNotDeleted(Long cursorId, Pageable pageable);

    Page<Post> findAllByMemberNotDeleted(Member member, Pageable pageable);

}
