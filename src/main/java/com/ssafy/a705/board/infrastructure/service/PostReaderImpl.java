package com.ssafy.a705.board.infrastructure.service;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.board.domain.exception.DeletedPostException;
import com.ssafy.a705.board.domain.exception.PostNotFoundException;
import com.ssafy.a705.board.domain.repository.PostRepository;
import com.ssafy.a705.board.domain.service.PostReader;
import com.ssafy.a705.domain.member.entity.Member;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostReaderImpl implements PostReader {

    private final PostRepository postRepository;

    @Override
    public Page<Post> getMemberPost(Member member, Pageable pageable) {
        return postRepository.findAllByMemberNotDeleted(member, pageable);
    }

    @Override
    public Post getPost(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new PostNotFoundException();
        }

        Post post = postOptional.get();
        if (Objects.isNull(post.getDeletedAt())) {
            return post;
        }

        throw new DeletedPostException();
    }

    @Override
    public List<Post> getPosts(Long cursorId, Pageable pageable) {
        return postRepository.findAllNotDeleted(cursorId, pageable);
    }
}
