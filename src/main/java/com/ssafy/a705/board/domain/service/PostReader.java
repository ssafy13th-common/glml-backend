package com.ssafy.a705.board.domain.service;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.board.domain.exception.DeletedPostException;
import com.ssafy.a705.board.domain.exception.PostNotFoundException;
import com.ssafy.a705.board.domain.repository.PostRepository;
import com.ssafy.a705.domain.member.entity.Member;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostReader {

    private final PostRepository postRepository;

    public Page<Post> getMemberPost(Member member, Pageable pageable) {
        return postRepository.findAllByMemberNotDeleted(member, pageable);
    }

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

    public List<Post> getPosts(Long cursorId, Pageable pageable) {
        return postRepository.findAllNotDeleted(cursorId, pageable);
    }
}
