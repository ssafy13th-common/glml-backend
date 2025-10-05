package com.ssafy.a705.board.domain.service;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.board.infrastructure.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostStore {

    private final PostJpaRepository postJpaRepository;

    public void savePost(Post post) {
        postJpaRepository.save(post);
    }

}
