package com.ssafy.a705.board.infrastructure.service;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.board.domain.repository.PostRepository;
import com.ssafy.a705.board.domain.service.PostStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostStoreImpl implements PostStore {

    private final PostRepository postRepository;

    @Override
    public void savePost(Post post) {
        postRepository.save(post);
    }

}
